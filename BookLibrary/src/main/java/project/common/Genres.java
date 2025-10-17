package project.common;

public class Genres {
	private String[] type ;
	
	public Genres(String[] genre) {this.setGenres(genre);}
	public Genres(String genre) {this.setGenres(genre);}
	public Genres() {type = new String[0];}
	
	public void setGenres(String[] genres) {
		if(genres != null)this.type = genres;
		else this.type = new String[0];
	}
	public void setGenres(String genres) {
		if(genres != null && !genres.isBlank()) this.type = genres.split(",");
		else this.type = new String[0];
	}
	
	public String[] toArray() {
		return this.type;
	}
	public String toString() {
		StringBuilder str = new StringBuilder();
		if(this.type.length > 0) {
			int count = this.type.length - 1;
			int i;
			for(i = 0 ; i <  count;i++)str.append(this.type[i] + ",");
			str.append(this.type[i]);
		}
		return str.toString();
	}
}
