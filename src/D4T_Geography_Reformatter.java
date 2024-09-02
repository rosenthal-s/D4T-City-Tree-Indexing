import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;



public class D4T_Geography_Reformatter {
    private static String[] ReadFile( String filePath )
    {
        String[] blocks = new String[0];

        try {
            BufferedReader fr = new BufferedReader( new FileReader( filePath ) );
            String line;

            fr.readLine(); // The first line is not needed
            line = fr.readLine(); // The second line contains all of the data

            blocks = line.split("><");
        }
        catch( FileNotFoundException e )
        {
            System.err.println( "Unable to open file '" + filePath + "'" );
            System.exit( 1 );
        }
        catch( IOException e )
        {
            System.err.println( "Error reading file '" + filePath + "'" );
            System.exit( 1 );
        }

        return blocks;
    }

    private static void WriteToFile( String filePath, ArrayList<String/*[]*/> cities )
    {
        try
        {
            BufferedWriter bw = new BufferedWriter( new FileWriter( filePath ) );

            bw.write( "City Id;City Name;Locale Id;Locale Name;Country Code;Country Name" );
            bw.newLine();

            for( String/*[]*/ city : cities )
            {
                bw.write( city );
                bw.newLine();
            }

            bw.flush();
            bw.close();
        }
        catch( IOException e )
        {
            System.err.println( "Error writing to file '" + filePath + "'" );
            System.exit( 1 );
        }
    }



    public static void main( String[] args )
    {
        Scanner sc = new Scanner( System.in );

        System.out.print( "Enter the input file name or path:\n" );
        String inputFilePath = sc.nextLine();

        String[] blocks = ReadFile( inputFilePath );



        ArrayList<String> cities = new ArrayList<>();

        boolean setCountry = false;
        String countryName = "";
        String countryCode = "";

        boolean setLocale = false;
        String localeName = "";
        String localeId = "";

        boolean setCity = false;
        String cityName = "";
        String cityId = "";

        for( String block : blocks )
        {
            if( block.equals( "Countries" ) || block.equals( "/Country" ) ) /// Make sure second check doesn't cause issues
            {
                setCountry = true;
            }
            else if( block.equals( "Locales" ) )
            {
                setLocale = true;
            }
            else if( block.equals( "Cities" ) )
            {
                setCity = true;
            }
            else if( block.equals( "/Cities" ) )
            {
                setCity = false;
            }
            else if( setCountry && !block.equals( "/Countries" ) ) // When block == "/Countries", we have reached the end of the document and there is no new country to set
            {
//                String[] subBlocks = block.split( " " );
                String[] subBlocks = block.split( "\"" );

//                for( String subBlock : subBlocks )
//                {
//                    if( subBlock.contains( "CountryCode" ) )
//                    {
//                        countryCode = subBlock.substring( 13, subBlock.length()-1 ); // Cut the `CountryName="..."` outer text, keeping only the country code
//                    }
//                    else if( subBlock.contains( "CountryName" ) ) /// This breaks when there's a space in the name. Also applies to locales and cities
//                    {
//                        countryName = subBlock.substring(13, subBlock.length()-1 ); // Cut the `CountryName="..."` outer text, keeping only the country name
//                    }
//                }
                // The country code is always the second block, and the country name is always the fourth
                countryCode = subBlocks[1];
                countryName = subBlocks[3];

                setCountry = false;
            }
            else if( setLocale )
            {
//                String[] subBlocks = block.split( " " );
                String[] subBlocks = block.split( "\"" );

//                for( String subBlock : subBlocks )
//                {
//                    if( subBlock.contains( "LocaleId" ) )
//                    {
//                        localeId = subBlock.substring( 10, subBlock.length()-1 ); // Cut the text, keeping only the locale id number
//                    }
//                    else if( subBlock.contains( "LocaleName" ) )
//                    {
//                        localeName = subBlock.substring(12, subBlock.length()-1 ); // Cut the `LocaleName="..."` outer text, keeping only the country name
//                    }
//                }
                // The locale id is always the second block, and the locale name is always the fourth
                localeId = subBlocks[1];
                localeName = subBlocks[3];

                setLocale = false;
            }
            else if( setCity )
            {
//                String[] subBlocks = block.split( " " );
                String[] subBlocks = block.split( "\"" );

//                for( String subBlock : subBlocks )
//                {
//                    if( subBlock.contains( "CityId" ) )
//                    {
//                        cityId = subBlock.substring( 8, subBlock.length()-1 ); // Cut the text, keeping only the city id number
//                    }
//                    else if( subBlock.contains( "CityName" ) )
//                    {
//                        cityName = subBlock.substring(10, subBlock.length()-1 ); // Cut the `CityName="..."` outer text, keeping only the city name
//                    }
//                }
                // The city id is always the second block, and the city name is always the fourth
                cityId = subBlocks[1];
                cityName = subBlocks[3];

                // Save city to array
//                cities.add( new String[] { cityId, cityName, localeId, localeName, countryCode, countryName } );
                cities.add( cityId + ";" + cityName + ";" + localeId + ";" + localeName + ";" + countryCode + ";" + countryName );
            }
            else if( block.equals("/Countries" ) )
            {
                // At block == "/Countries", we have reached the end of any meaningful data
                break;
            }
        }



        System.out.print( "\n\nEnter the output file name or path:\n" );
        String outputFilePath = sc.nextLine();

        WriteToFile( outputFilePath, cities );
    }
}
