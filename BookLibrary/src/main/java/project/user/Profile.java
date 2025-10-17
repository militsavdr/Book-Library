package project.user;

import java.util.ArrayList;

import project.common.Genres;

public class Profile {
	
	private int UserID;
	private int age;
    private String FullName;
    private String address;
    private String phone_number;
    private String fav_authors;
    private boolean adventure;
    private boolean fiction;
    private boolean crime;
    private boolean art;
    private boolean comic;
    private boolean fantasy;
    private boolean biography;
    private boolean history;
    private boolean science;
    private boolean literature;
    private boolean other;
    
    public Profile() {
    }
    
    private Genres makeGenre() {
    	ArrayList<String> str = new ArrayList<String>();
    	if(adventure)str.add("Adventure");
    	if(crime)str.add("Crime");
    	if(fiction)str.add("Fiction");
    	if(art)str.add("Art");
    	if(comic)str.add("Comic");
    	if(fantasy)str.add("Fantasy");
    	if(biography)str.add("Biography");
    	if(history)str.add("History");
    	if(science)str.add("Science");
    	if(literature)str.add("Literature");
    	if(other)str.add("Other");
    	int count = str.size();
    	String[] result = new String[count];
    	for(int i = 0;  i < count ;i++)result[i] = str.get(i);
    	return new Genres(result);
    }
    
    private void setGenre(String[] context) {
    	for(int i = 0 ; i < context.length ;i++)
	    	if(context[i].equals("Adventure"))adventure = true;
	    	else if(context[i].equals("Crime"))crime = true;
	    	else if(context[i].equals("Fiction"))fiction = true;
	    	else if(context[i].equals("Art"))art =  true;
	    	else if(context[i].equals("Comic"))comic = true;
	    	else if(context[i].equals("Fantasy"))fantasy = true;
	    	else if(context[i].equals("Biography"))biography = true;
	    	else if(context[i].equals("History"))history = true;
	    	else if(context[i].equals("Science"))science = true;
	    	else if(context[i].equals("Literature"))literature = true;
	    	else if(context[i].equals("Other")) other = true;
    }
    
	public void setFullName(String FullName) {if (!FullName.isBlank() && !FullName.matches("^[^0-9]*[0-9]+[^0-9]*$") && FullName.length() <= 60)this.FullName = FullName;}
	public void setAddress(String address) {if(!address.isBlank() && address.length() <= 45)this.address = address;}
	public void setPhone_number(String phone_number) {if(phone_number.matches("^(\\+[0-9]{2}\s)?[0-9]{10}$"))this.phone_number = phone_number;}
	public void setFav_authors(String fav_authors) {this.fav_authors = fav_authors;}
	public void setAge(int age) {this.age = age;}
	public void setAdventure(boolean adventure) {this.adventure = adventure;}
	public void setFiction(boolean fiction) {this.fiction = fiction;}
	public void setCrime(boolean crime) {this.crime = crime;}
	public void setArt(boolean art) {this.art = art;}
	public void setComic(boolean comic) {this.comic = comic;}
	public void setFantasy(boolean fantasy) {this.fantasy = fantasy;}
	public void setBiography(boolean biography) {this.biography = biography;}
	public void setHistory(boolean history) {this.history = history;}
	public void setScience(boolean science) {this.science = science;}
	public void setLiterature(boolean literature) {this.literature = literature;}
	public void setOther(boolean other) {this.other = other;}
	
	public Profile(int ID,int age,String FullName,String address,String phone_number,String fav_genres,String fav_authors) {
    	this.UserID = ID;
    	this.age = age;
    	this.FullName = FullName;
    	this.address = address;
    	this.phone_number = phone_number;
    	this.setGenre(fav_genres.split(","));
    	this.fav_authors = fav_authors;
    }
	
	public boolean isAdventure() {return adventure;}
	public boolean isFiction() {return fiction;}
	public boolean isCrime() {return crime;}
	public boolean isArt() {return art;}
	public boolean isComic() {return comic;}
	public boolean isFantasy() {return fantasy;}
	public boolean isBiography() {return biography;}
	public boolean isHistory() {return history;}
	public boolean isScience() {return science;}
	public boolean isLiterature() {return literature;}
	public boolean isOther() {return other;}
	public String getFullName() {return FullName;}
	public int getAge() {return age;}
	public int getID() {return this.UserID;}
	public String getAddress() {return address;}
	public String getPhone_number() {return phone_number;}
	public String getFav_genres() {return this.makeGenre().toString();}
	public String[] getFav_array() {return this.makeGenre().toArray();}
	
	
	public String getFav_authors() {
		return this.fav_authors;
	}
	public String[] getFav_authorsArray() {
		if (fav_authors == null || fav_authors.isBlank())return new String[0];
		else return this.fav_authors.split(",");
	}
}
