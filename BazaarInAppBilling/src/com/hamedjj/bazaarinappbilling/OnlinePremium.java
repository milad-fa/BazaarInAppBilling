package com.hamedjj.bazaarinappbilling;

import com.hamedjj.bazaarinappbilling.util.IabHelper;
import com.hamedjj.bazaarinappbilling.util.IabResult;
import com.hamedjj.bazaarinappbilling.util.Inventory;
import com.hamedjj.bazaarinappbilling.util.Purchase;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class OnlinePremium extends Activity{
	
	public ProgressDialog dialog ;
	
	Button btn1;
	
	// Debug tag, for logging
	static final String TAG = "onlinePremium";

	// SKUs for our products: the premium upgrade (non-consumable)
	static final String SKU_PREMIUM = "online";

	// Does the user have the premium upgrade?
	boolean mIsPremium = false;

	// (arbitrary) request code for the purchase flow
	static final int RC_REQUEST = 10001;

	// The helper object
	IabHelper mHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.onlinepremium);
		btn1 = (Button) findViewById(R.id.onlinebtn);
		
		// show loading dialog with ProgressDialog
		dialog = new ProgressDialog(this);
		dialog.setMessage("loading...");
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);
        dialog.show();

		
		String base64EncodedPublicKey = "MIHNMA0GCSqGSIb3DQEBAQUAA4G7ADCBtwKBrwDGhl8/QU3vPjgmTutAbItCBpdwwgFWSAFvzM/OOXVSMHaeH9fjRIxa3aLXVAfuoRJ3Q1ynbQL1Dc2hAvlTAgEeRFNmVkkjypzhZxK3O18wIYJiNleLd/pXZyWaoHeQB6s3eH3KB8uDn2TdZoYzmXxZkvMoDW2db3mT1NmPxJYm+xF7AN/p/Sr9YqEXpIpzsXbe6T30seUHmPDdM4r7h/r6hx/R/2hHvR/vnN0i6w8CAwEAAQ==";
		// You can find it in your Bazaar console, in the Dealers section. 
		// It is recommended to add more security than just pasting it in your source code;
		mHelper = new IabHelper(this, base64EncodedPublicKey);

		
		Log.d(TAG, "Starting setup.");		
		mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
		    public void onIabSetupFinished(IabResult result) {
		        Log.d(TAG, "Setup finished.");

		        if (!result.isSuccess()) {
		            // Oh noes, there was a problem.
		            Log.d(TAG, "Problem setting up In-app Billing: " + result);
		        }
		        // Hooray, IAB is fully set up!
		        mHelper.queryInventoryAsync(mGotInventoryListener);
		        
		    }
		});
	}
	
	IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
	    public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
	        Log.d(TAG, "Query inventory finished.");
	        if (result.isFailure()) {
	            Log.d(TAG, "Failed to query inventory: " + result);
	            dialog.hide();
	            return;
	        }
	        else {
	            Log.d(TAG, "Query inventory was successful.");
	            // does the user have the premium upgrade?
	            mIsPremium = inventory.hasPurchase(SKU_PREMIUM);

	            // update UI accordingly

	            Log.d(TAG, "User is " + (mIsPremium ? "PREMIUM" : "NOT PREMIUM"));
	        }
	        dialog.hide();
	        updateUi();
            setWaitScreen(false);
            Toast.makeText(getApplicationContext(), mIsPremium? R.string.premium : R.string.notpremium, Toast.LENGTH_SHORT).show();
	        Log.d(TAG, "Initial inventory query finished; enabling main UI.");
	        
	    }
	    
	};
		
	
    public void onOnlineUpgradeAppButtonClicked(View arg0) {
        Log.d(TAG, "Upgrade button clicked; launching purchase flow for upgrade.");
        setWaitScreen(true);
        
        /* TODO: for security, generate your payload here for verification. See the comments on
         * verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
         * an empty string, but on a production app you should carefully generate this. */
        String payload = "inbarnametavasotehamedjjsakhteshodeast";

        mHelper.launchPurchaseFlow(this, SKU_PREMIUM, RC_REQUEST,
                mPurchaseFinishedListener, payload);
    }
	
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);

	    Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);

	    // Pass on the activity result to the helper for handling
	    if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
	        super.onActivityResult(requestCode, resultCode, data);
	    } else {
	        Log.d(TAG, "onActivityResult handled by IABUtil.");
	    }
	        
	}
		
	boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();
               
        return true;
    }
	
	IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
	    public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
	    	Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);
	    	if (result.isFailure()) {
	            Log.d(TAG, "Error purchasing: " + result);
	            setWaitScreen(false);
	            return;
	        }
	    	 if (!verifyDeveloperPayload(purchase)) {
	                complain("Error purchasing. Authenticity verification failed.");
	                setWaitScreen(false);
	                return;
	    	 }
	    	
	    	 Log.d(TAG, "Purchase successful.");
	    	
	    	 if (purchase.getSku().equals(SKU_PREMIUM)) {
	    		 Log.d(TAG, "Purchase is premium upgrade. Congratulating user.");
	                alert("Thank you for upgrading to premium!");
	                mIsPremium = true;
	                updateUi();
	                setWaitScreen(false);
	                savedata();
	        }
	    }
	};
	
	@Override
	public void onDestroy() {
	    super.onDestroy();
	    
	    Log.d(TAG, "Destroying helper.");
	    if (mHelper != null) mHelper.dispose();
	    mHelper = null;
	}
	// Update button with updateUi
	public void updateUi() {
	
		if (mIsPremium) {
			findViewById(R.id.onlinebtn).setBackgroundResource(R.drawable.green);
			btn1.setOnClickListener(new OnClickListener() {			
				@Override
				public void onClick(View v) {
					Toast.makeText(getApplicationContext(), R.string.clickpremium, Toast.LENGTH_SHORT).show();			
				}
			});
		
	}
}

// Enables or disables the "please wait" screen.
	void setWaitScreen(boolean set) {
	    findViewById(R.id.screen_wait).setVisibility(set ? View.VISIBLE : View.GONE);
	}
	
	void alertdialog(boolean b) {
		ProgressDialog dialog = new ProgressDialog(this);
		dialog.setMessage("loading");
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);
        dialog.show();
	}
	
	void complain(String message) {
	    Log.e(TAG, "**** testbilling Error: " + message);
	    alert("Error: " + message);
	}
	
	void alert(String message) {
	    AlertDialog.Builder bld = new AlertDialog.Builder(this);
	    bld.setMessage(message);
	    bld.setNeutralButton("OK", null);
	    Log.d(TAG, "Showing alert dialog: " + message);
	    bld.create().show();

	}	
		
		
		void savedata() {
			SharedPreferences settings = getSharedPreferences("MUPREF", 0);
			SharedPreferences.Editor editor = settings.edit();
			editor.putBoolean("update", true);
			editor.commit();
			
			Log.d(TAG, "changed!!");
	}

}
