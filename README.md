habrtimezone
============

An example how to work using Google Maps, time zones, geonames.org

First of all, register Google Maps API-Key here: https://code.google.com/apis/console/b/0/
Help: https://developers.google.com/maps/documentation/android/start

Paste API-key into AndroidManifest.xml here:
<meta-data
            android:name="com.google.android.maps.v2.API_KEY"
            android:value="your_api_key" />

Than, register and activate account(s) on GeoNames.org: http://www.geonames.org/login

write one or several accounts into this string in "\_.java" file:
public static final String[] names1 = {"your_account_1","your_account_2"};//ad infinitum

Complite and enjoy.
