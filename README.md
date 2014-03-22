<div lang="fa" dir="rtl">
<h1>پرداخت درون برنامه ای بازار</h1>
<p><strong>
پرداخت درون برنامه‌ای یکی از سرویس‌های بازار است که فروش محتوای دیجیتال از درون برنامه‌ها را ممکن می‌کند. شما می‌توانید از این سرویس برای فروش محتوای مختلف مانند محتوای قابل دانلود مثل موسیقی، تصاویر و محتوای غیر قابل دانلود مثل افزایش مرحله یا خرید سکه در بازی‌ها استفاده کنید. 
</p></strong>
<p><strong>
وقتی از پرداخت درون برنامه‌ای بازار برای فروش محتوا استفاده می‌کنید، کلیهٔ مراحل پرداخت توسط بازار انجام می‌شود. بنابراین نیازی نیست برنامهٔ شما به صورت مستقیم پردازش مالی پرداخت‌ها را انجام دهد.
</p></strong>
<h2>معرفی</h2>
<p><strong>
در این پروژه آموزشی استفاده از پرداخت درون برنامه ای بازار به شکل بسیار ساده پیاده سازی شده تا شما هم بتوانید این سیستم را در برنامه خودتان پیاده سازی کنید.<br>
این پروژه فقط نحوه ارتقای کاربران(premium) را در خود جای داده اما احتمالا استفاده از این سیستم در کالاهای مصرفی هم در آینده آموزش داده خواهد شد.
</p></strong></div>
<p align="center"><strong>
<img src="http://axgig.com/images/19171236564181435574.png" border="0" alt="image1" width="160" height="267" />
 --> 
<img src="http://axgig.com/images/59827746574750759720.png" border="0" alt="image2" width="160" height="267" />
 --> 
<img src="http://axgig.com/images/72349850009242691162.png" border="0" alt="image3" width="160" height="267" />
 --> 
<img src="http://axgig.com/images/25090378793605189358.png" border="0" alt="image4" width="160" height="267" />
</p></strong>
<div lang="fa" dir="rtl">
<h2>آموزش</h2>
<p><strong>
ما برای ساخت این برنامه از پروژه TriviaDrive و <a href="http://pardakht.cafebazaar.ir/doc/quickstart/?l=fa">راهنمای یک شروع سریع</a> در بازار استفاده کردیم و به شما هم 100% تاکید میکنم که این صفحه را دقیقا مطالعه نمایید.
<br>
من فقط در اینجا توضیحاتی از عملکرد برنامه به شما میدم و جزئیات برنامه را میتوانید در وبسایت مارکت بازار مطالعه نمایید
<br><br>
اول باید فایل هایی که از طرف بازار معرفی شده را وارد پروژه خود کنید و مجوز زیر را در مانیفست برنامه خود قرار دهید
<br>
<code>android:name="com.farsitel.bazaar.permission.PAY_THROUGH_BAZAAR"</code>
<br>
بعد از برنامه خود خروجی بگیرید و در پنل کاربری خود در بازار آپلود کنید تا قسمت پرداخت درون برنامه ای باز شود.
<>
وارد شوید و محصولات خود را ثبت کنید..<br> <a href="http://pardakht.cafebazaar.ir/doc/billing-admin/?l=fa">آموزش ثبت محصول</a>
<br><br>
در پروژه اکتیویتی <code>OnlinePremium.java</code> کاربر بعد از ارتقاء حساب خود برای دفعه های بعد که وارد برنامه میشود باید به اینترنت متصل باشد تا اکانت کاربری او توسط سیستم بازار شناسایی شود و بازار تایید کنید که آیا او ویژه هست یا خیر (تمام این کارها در چند ثانیه انجام میشود)
<br>در بالای متد <code>onCreate</code> شما باید شناسه کالا خود را در این قسمت قرار دهید <code>static final String SKU_PREMIUM = "";</code><br>
همچنین باید در داخل متد  <code>onCreate</code> کلید عمومی برنامه را در این قسمت قرار دهید <code>String base64EncodedPublicKey = ""</code><br>
در آغاز ورود به اکتیویتی <code>OnlinePremium.java</code> دیالوگی ظاهر میشه که بعد از شناسایی کاربر از premium بودن یا نه ناپدید میشه و نتیجه را در قالب یک <code>Toast</code> نمایش میدهد.<br>
</p></strong></div>

```java
	 dialog = new ProgressDialog(this);
     dialog.setMessage("loading...");
     dialog.setCancelable(false);
     dialog.setInverseBackgroundForced(false);
     dialog.show();
```
<br><div lang="fa" dir="rtl">
<p><strong>
با آغاز اکتیویتی کد زیر اجرا میشود که برای جستجو premium بودن میباشد.<br>
اگر کاربر premium باشد دیالوگ loading مخفی میشه <code>dialog.hide()</code> متد <code>UpdateUi()</code> اجرا میشه و محتوای این متد را در باتن قرار میده.<br>
اگر کاربر premium نباشه هم منتظر لمس کردن کاربر بر روی دکمه میشه که متد <code>onOnlineUpgradeAppButtonClicked</code> را فراخوانی کند.
</p></strong></div>
```java
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
```
<br><div lang="fa" dir="rtl">
<p><strong>
با لمس کردن باتن متد <code>onOnlineUpgradeAppButtonClicked</code> که ما در فایل <code>onlinepremium.xml</code> برای خاصیت <code>android:onClick</code> قرار دادیم تا با لمس دکمه متد ذکر شده اجرا بشه
</p></strong></div>
```java

    public void onOnlineUpgradeAppButtonClicked(View arg0) {
        Log.d(TAG, "Upgrade button clicked; launching purchase flow for upgrade.");
        setWaitScreen(true);
        
        String payload = "inbarnametavasotehamedjjsakhteshodeast";

        mHelper.launchPurchaseFlow(this, SKU_PREMIUM, RC_REQUEST,
                mPurchaseFinishedListener, payload);
    }
```

```java
    <Button
        android:id="@+id/onlinebtn"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_alignParentTop="true"
        android:layout_centerHorizontal="true"
        android:layout_marginTop="137dp"
        android:onClick="onOnlineUpgradeAppButtonClicked"
        android:text="online premium" />
```
<br><div lang="fa" dir="rtl">
<p><strong>
بعد از پرداخت همه چیز به متد <code>updateUi</code> برمیگرده<br>
پس شما هر دستوری در این متد قرار بدهید بعد از پرداخت و ویژه شدن کاربر تمامی دستورات این متد اجرا میشود.<br>
ما در این پروژه رنگ باتن را عوض میکنم و یک <code>Toast</code> با کلیک بر روی باتن نمایش میدم
</p></strong></div>
```java
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
```
<br><div lang="fa" dir="rtl">
<p><strong>
در اکتیویتی <code>Premium.java</code> تمامی کدها و دستورات با اکتیویتی <code>OnlinePremium.java</code> یکی است فقط ما در در این اکتیویتی خاصیت <code>SharedPreferences</code> قرار دادیم تا بعد از اولین بار پرداخت و ارتقاء یافتن کاربر این دیتا در برنامه ذخیره شود تا بعد از هر بار ورود به برنامه نیازی به اینترنت و چک کردن کاربر توسط بازار نباشد.<br>
<b>توجه :</b> این روش امنی نیست و ما روش اول را برای شما پیشنهاد میکنیم<br><br>
برای این کار متد <code>mIsPremium</code> را در اول برابر <code>false</code> قرار دادیم وگفتیم که اگر <code>mIsPremium == true</code> متد <code>updateUi()</code> را اجرا کند.
</p></strong></div>
```java
preferences = getSharedPreferences(PACKAGENAME,Context.MODE_PRIVATE);
PACKAGENAME = getClass().getName();
Log.e("TAG", PACKAGENAME);
mIsPremium = preferences.getBoolean(KEY, false);
      if (mIsPremium == true) {
          updateUi();
          return;
      }
```
<br><div lang="fa" dir="rtl">
<p><strong>
برای بار اول که کاربر ویژه نیست بعد از ارتقاء حساب متد <code>updateUi()</code> انجام میشه که ما برای تغییر محتوای <code>SharedPreferences</code> کد <code>SharedPreferences.Editor</code> را در این متد قرار دادیم تا با تغییر باتن محتوای <code>SharedPreferences</code> هم <code>true</code> شود و برای دفعه های بعد تنظیمات ویژه بودن کاربر در برنامه ذخیره بماند.
</p></strong></div>
```java
public void updateUi() {

    if (mIsPremium) {
         findViewById(R.id.onlinebtn).setBackgroundResource(R.drawable.green);
         btn1.setOnClickListener(new OnClickListener() {	
            @Override
            public void onClick(View v) {
            Toast.makeText(getApplicationContext(), R.string.clickpremium, Toast.LENGTH_SHORT).show();	
            }
         });
         // change the mIsPremium to true
         SharedPreferences.Editor newtask = preferences.edit();
         newtask.putBoolean(KEY, true);
         newtask.commit();
    }
 }
```

<br><div lang="fa" dir="rtl">
<p><strong>
شما میتوانید این پروژه را fork کنید و در کامل کردن این پروژه ما را یاری کنید
<br>
همچنین با ستاره (در بالای صفحه) دلگرمی ما برای پروژه های آموزشی بعدی باشید.<br>
امیدوارم با این آموزش قطره ای به دانش اندرویدی شما افزوده باشم
<h2>نویسنده</h2>
<p><strong>
<img src="http://www.axgig.com/images/92193400440163114768.png" border="0" alt="hamedjj" align="middle" width="50" height="50" /> <a href="http://barnamenevis.org/member.php?290105-hamedjj">hamedjj</a>
</p></strong></div>
