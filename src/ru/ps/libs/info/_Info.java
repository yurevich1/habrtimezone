package ru.ps.libs.info;

import android.os.Parcel;
import android.os.Parcelable;

public class _Info implements Parcelable{
	double SCALEDIV= 10000000.;
	private long geonameid;//: integer id of record in geonames database
	private String name;//: name of geographical point (utf8) varchar(200)
	private String asciiname;//: name of geographical point in plain ascii characters, varchar(200)
	private String alternatenames;//: alternatenames, comma separated varchar(5000)
	private double latitude;//: latitude in decimal degrees (wgs84)
	private double longitude;//: longitude in decimal degrees (wgs84)
	private String feature_class ;//: see http://www.geonames.org/export/codes.html, char(1)
	private String feature_code;//: see http://www.geonames.org/export/codes.html, varchar(10)
	private String country_code;//: ISO-3166 2-letter country code, 2 characters
	private String cc2;//: alternate country codes, comma separated, ISO-3166 2-letter country code, 60 characters
	private String admin1_code ;//: fipscode (subject to change to iso code), see exceptions below, see file admin1Codes.txt for display names of this code; varchar(20)
	private String admin2_code ;//: code for the second administrative division, a county in the US, see file admin2Codes.txt; varchar(80) 
	private String admin3_code ;//: code for third level administrative division, varchar(20)
	private String admin4_code;//: code for fourth level administrative division, varchar(20)
	private long population;//: bigint (8 byte int) 
	private long elevation;//: in meters, integer
	private String dem;//: digital elevation model, srtm3 or gtopo30, average elevation of 3''x3'' (ca 90mx90m) or 30''x30'' (ca 900mx900m) area in meters, integer. srtm processed by cgiar/ciat.
	private String timezone;//: the timezone id (see file timeZone.txt) varchar(40)
	private long modification_date;//: date of last modification in yyyy-MM-dd format
	public _Info()
	{
	this.setGeonameid(0L);
	this.setName("");
	this.setAsciiname("");
	this.setAlternatenames("");
	this.setLatitude(0.);
	this.setLongitude(0.);
	this.setFeature_class("");
	this.setFeature_code("");
	this.setCountry_code("");
	this.setCc2("");
	this.setAdmin1_code("");
	this.setAdmin2_code("");
	this.setAdmin3_code("");
	this.setAdmin4_code("");
	this.setPopulation(0L);
	this.setElevation(0L);
	this.setDem("");
	this.setTimezone("");
	this.setModification_date(0L);
	}
	public _Info(long geonameid, String name, String asciiname, String alternatenames, double latitude, double longitude, String feature_class, String feature_code, String country_code, String cc2, String admin1_code, String admin2_code, String admin3_code, String admin4_code, long population, long elevation, String dem, String timezone, long modification_date)
	{
	this.setGeonameid(geonameid);
	this.setName(name);
	this.setAsciiname(asciiname);
	this.setAlternatenames(alternatenames);
	this.setLatitude(latitude);
	this.setLongitude(longitude);
	this.setFeature_class(feature_class);
	this.setFeature_code(feature_code);
	this.setCountry_code(country_code);
	this.setCc2(cc2);
	this.setAdmin1_code(admin1_code);
	this.setAdmin2_code(admin2_code);
	this.setAdmin3_code(admin3_code);
	this.setAdmin4_code(admin4_code);
	this.setPopulation(population);
	this.setElevation(elevation);
	this.setDem(dem);
	this.setTimezone(timezone);
	this.setModification_date(modification_date);
	}
	public long getGeonameid() {
		return geonameid;
	}
	public void setGeonameid(long geonameid) {
		this.geonameid = geonameid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAsciiname() {
		return asciiname;
	}
	public void setAsciiname(String asciiname) {
		this.asciiname = asciiname;
	}
	public String getAlternatenames() {
		return alternatenames;
	}
	public void setAlternatenames(String alternatenames) {
		this.alternatenames = alternatenames;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public String getFeature_class() {
		return feature_class;
	}
	public void setFeature_class(String feature_class) {
		this.feature_class = feature_class;
	}
	public String getFeature_code() {
		return feature_code;
	}
	public void setFeature_code(String feature_code) {
		this.feature_code = feature_code;
	}
	public String getCountry_code() {
		return country_code;
	}
	public void setCountry_code(String country_code) {
		this.country_code = country_code;
	}
	public String getCc2() {
		return cc2;
	}
	public void setCc2(String cc2) {
		this.cc2 = cc2;
	}
	public String getAdmin1_code() {
		return admin1_code;
	}
	public void setAdmin1_code(String admin1_code) {
		this.admin1_code = admin1_code;
	}
	public String getAdmin2_code() {
		return admin2_code;
	}
	public void setAdmin2_code(String admin2_code) {
		this.admin2_code = admin2_code;
	}
	public String getAdmin3_code() {
		return admin3_code;
	}
	public void setAdmin3_code(String admin3_code) {
		this.admin3_code = admin3_code;
	}
	public String getAdmin4_code() {
		return admin4_code;
	}
	public void setAdmin4_code(String admin4_code) {
		this.admin4_code = admin4_code;
	}
	public long getPopulation() {
		return population;
	}
	public void setPopulation(long population) {
		this.population = population;
	}
	public long getElevation() {
		return elevation;
	}
	public void setElevation(long elevation) {
		this.elevation = elevation;
	}
	public String getDem() {
		return dem;
	}
	public void setDem(String dem) {
		this.dem = dem;
	}
	public String getTimezone() {
		return timezone;
	}
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}
	public long getModification_date() {
		return modification_date;
	}
	public void setModification_date(long modification_date) {
		this.modification_date = modification_date;
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeLong(geonameid);
		out.writeString(name);
		out.writeString(asciiname);
		out.writeString(alternatenames);
		out.writeDouble(latitude);
		out.writeDouble(longitude);
		out.writeString(feature_class);
		out.writeString(feature_code);
		out.writeString(country_code);
		out.writeString(cc2);
		out.writeString(admin1_code);
		out.writeString(admin2_code);
		out.writeString(admin3_code);
		out.writeString(admin4_code);
		out.writeLong(population);
		out.writeLong(elevation);
		out.writeString(dem);
		out.writeString(timezone);
		out.writeLong(modification_date);
	}
	public static final Parcelable.Creator<_Info> CREATOR
	= new Parcelable.Creator<_Info>() {
	
	public _Info createFromParcel(Parcel in) {
			return new _Info(in);
		}

	public _Info[] newArray(int size) {
			return new _Info[size];
		}
	};
	private _Info(Parcel in) {
		geonameid = in.readLong();
		name = in.readString();
		asciiname = in.readString();
		alternatenames = in.readString();
		latitude = in.readDouble(); 
		longitude = in.readDouble();
		feature_class = in.readString();
		feature_code = in.readString();
		country_code = in.readString();
		cc2 = in.readString();
		admin1_code = in.readString();
		admin2_code = in.readString();
		admin3_code = in.readString();
		admin4_code = in.readString();
		population = in.readLong();
		elevation = in.readLong();
		dem = in.readString();
		timezone = in.readString();
		modification_date = in.readLong();
	}
	public Long getLong(int i)
	{
		switch (i)
		{
		case 0:
			return (geonameid);
		case 4:
			return (long)(latitude * SCALEDIV);
		case 5:
			return (long)(longitude * SCALEDIV);
		case 14:
			return population;
		case 15:
			return elevation;
		case 18:
			return modification_date;
		default:
			return 0L;
		}
	}
	public Double getDouble(int i)
	{
		switch (i)
		{
		case 4:
			return latitude;
		case 5:
			return longitude;
		default:
			return 0.;
		}
	}
	public String getString(int i)
	{
		switch (i)
		{
		case 0:
			return Long.toString(geonameid);
		case 1:
			return name;
		case 2:
			return asciiname;
		case 3:
			return alternatenames;
		case 4:
			return Double.toString(latitude);
		case 5:
			return Double.toString(longitude);
		case 6:
			return feature_class;
		case 7:
			return feature_code;
		case 8:
			return country_code;
		case 9:
			return cc2;
		case 10:
			return admin1_code;
		case 11:
			return admin2_code;
		case 12:
			return admin3_code;
		case 13:
			return admin4_code;
		case 14:
			return Long.toString(population);
		case 15:
			return Long.toString(elevation);
		case 16:
			return dem;
		case 17:
			return timezone;
		case 18:
			return Long.toString(modification_date);
		default:
			return "";
		}
	}
	public int size()
	{
		return 19;
	}
	
}
