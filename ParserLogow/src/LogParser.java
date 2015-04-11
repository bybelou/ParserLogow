import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class LogParser {

	public static void main(String[] args) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new FileReader("MI_Trace.log"));
		PrintWriter bufferedWriter = new PrintWriter("MI_SQLs.sql");
		
		boolean sqlQuery = false;
		boolean copying = false;
		boolean previousBlank = false;
		String textLine = bufferedReader.readLine();
		//jesli nie koniec pliku
		while (textLine != null){
			//jesli jest to zapytanie zbindowane, INSERT lub UPDATE
			if (textLine.contains("SQL: Bind:")
				&& (textLine.contains("SQL="+new String (Character.toChars(34))+"INSERT")
					|| textLine.contains("SQL="+new String (Character.toChars(34))+"UPDATE")
					)
				){
				//linijka rozpoczynajaca SQL
				System.out.println(textLine);
				sqlQuery = true;
			}
			
			//jesli to SQL
			if (sqlQuery){
				for (int i=0; i<textLine.length(); i++){
					//jesli juz jest w srodku " i znalazl ostatni " to przerwac
					if (copying==true && textLine.charAt(i)=='"'){
						copying = false;
						sqlQuery = false;
						bufferedWriter.println(';');
					}
					//jesli ma dodawac znaki SQL
					if (copying){
						//jesli obecnie jest inny znak niz spacja, a poprzednia byla spacja - zeruj zmienna
						if (textLine.charAt(i) != ' ' && previousBlank==true) previousBlank=false;
						//jesli poprzednia nie byla spacja i nie jest TABULATOR - dodaj znak
						if (!previousBlank && textLine.charAt(i)!='	') bufferedWriter.print(textLine.charAt(i));
						//jesli znak jest spacja to ustaw flage
						if (textLine.charAt(i) == ' ') previousBlank=true;
					}
					
					//jesli jest to SQL i jeszcze nie kopiowal a natrafil na " - zacznij kopiowac
					if (sqlQuery==true && copying==false && textLine.charAt(i)=='"') copying=true;
				}
			}
			//wczytaj kolejna linie
			textLine = bufferedReader.readLine();
		}
		
		//zamknij bufory
		bufferedReader.close();
		bufferedWriter.close();
	}
}
