package ru.ps.habrtimezone;

import android.app.Application;

public class _ extends Application {

	public static final String LATGMAP = "LATGMAP";
	public static final String LONGMAP = "LONGMAP";
	public static final String LATGMAPCUR = "LATGMAPCUR";
	public static final String LONGMAPCUR = "LONGMAPCUR";
	public static final String CURORMAN = "CURORMAN";
	public static final String TIMEZONE = "TIMEZONE";
	public static final String ERRORTZ = "ERRORTZ";
	public static final String NAMEOFTHEPLACE = "NAMEOFTHEPLACE";
	public static final String FULLDATA = "FULLDATA";
	public static final int agmap_activity = 0;
	public static final int acities_activity = 1;
	public static final long TIMEUPDATE = 5000L;
	public static int useGPS = 1;

	public static String geonameid         = "geonameid";//: integer id of record in geonames database
	public static String name              = "name";//: name of geographical point (utf8) varchar(200)
	public static String asciiname         = "asciiname";//: name of geographical point in plain ascii characters, varchar(200)
	public static String alternatenames    = "alternatenames";//: alternatenames, comma separated varchar(5000)
	public static String latitude          = "latitude";//: latitude in decimal degrees (wgs84)
	public static String longitude         = "longitude";//: longitude in decimal degrees (wgs84)
	public static String feature_class     = "featureclass";//: see http://www.geonames.org/export/codes.html, char(1)
	public static String feature_code      = "featurecode";//: see http://www.geonames.org/export/codes.html, varchar(10)
	public static String country_code      = "countrycode";//: ISO-3166 2-letter country code, 2 characters
	public static String cc2               = "cc2";//: alternate country codes, comma separated, ISO-3166 2-letter country code, 60 characters
	public static String admin1_code       = "admin1code";//: fipscode (subject to change to iso code), see exceptions below, see file admin1Codes.txt for display names of this code; varchar(20)
	public static String admin2_code       = "admin2code";//: code for the second administrative division, a county in the US, see file admin2Codes.txt; varchar(80) 
	public static String admin3_code       = "admin3code";//: code for third level administrative division, varchar(20)
	public static String admin4_code       = "admin4code";//: code for fourth level administrative division, varchar(20)
	public static String population        = "population";//: bigint (8 byte int) 
	public static String elevation         = "elevation";//: in meters, integer
	public static String dem               = "dem";//: digital elevation model, srtm3 or gtopo30, average elevation of 3''x3'' (ca 90mx90m) or 30''x30'' (ca 900mx900m) area in meters, integer. srtm processed by cgiar/ciat.
	public static String timezone          = "timezone";//: the timezone id (see file timeZone.txt) varchar(40)
	public static String modification_date = "modificationdate";//: date of last modification in yyyy-MM-dd format
	
	public static final String[] names1 = {};
	public static String[] names = {
		geonameid,
		name,
		asciiname,
		alternatenames,
		latitude,
		longitude,
		feature_class,
		feature_code,
		country_code,
		cc2,
		admin1_code,
		admin2_code,
		admin3_code,
		admin4_code,
		population,
		elevation,
		dem,
		timezone,
		modification_date
		};
	
	public static String todeg(double input, int i,String[] ew_ar,String[] ns_ar) {
		// TODO Auto-generated method stub
		//String[] ew_ar = {"E","W"};
		//String[] ns_ar = {"N","S"};
		double MINS = 60.;
		double SECS = 3600.;
		long SCALESEC = 100L;
		
		
		double a = Math.abs(input);
		long deg = (long) (a);
		long min = (long) (((double) (a - deg)) * ((double) MINS));
		double sec = ((double) ((long) (((double) (a - deg - min / (double) MINS)) * ((double) SECS) * ((double) SCALESEC)))) / ((double) SCALESEC);
		String dir =  "";

		if (i == 0) dir += ((input>=0)?ew_ar[0]:ew_ar[1]);
		if (i == 1) dir += ((input>=0)?ns_ar[0]:ns_ar[1]);
		return Long.toString(deg) + "°" + " " + ((min < 10)?"0":"") + Long.toString(min) + "\'" + " " + ((sec < 10)?"0":"") + Double.toString(sec) + "\"" + " " + dir;
	}
}
