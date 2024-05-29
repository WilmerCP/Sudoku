package com.medeniyet;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import javax.net.ssl.HttpsURLConnection;
import javax.swing.*;
import java.io.*;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URL;
import java.util.*;

//Swing worker class allows us to execute the API calls in a different worker thread
//This way the Event Dispatcher thread where the GUI is running is not blocked
public class SudokuFetcher extends SwingWorker<Void,Void> implements Serializable {

    private String difficulty;
    private Map<String,Object> sudokuData;
    private int[][] sudoku;
    private int[][] solution;
    private int[] sudokuArray;
    private int[] solutionArray;
    private GameWindow window;
    private int requestAmount = 0;

    SudokuFetcher(String difficulty, GameWindow window){

        this.difficulty = difficulty;
        this.window = window;

    }

    @Override
    protected Void doInBackground(){

        getSudoku();

        return null;
    }

    @Override
    protected  void done(){

        for (int i = 0; i<81;i++){

            window.getCells().get(i).setValue(this.sudokuArray[i]);
            window.getCells().get(i).setCorrectValue(this.solutionArray[i]);


        }

        window.checkDifficulty();

        for (int i = 0; i<81;i++){

            window.getCells().get(i).listen();

        }

        window.checkSudoku(this.solutionArray);
        window.setSolutionArray(this.solutionArray);
        window.startTimer(window.getTimeLimit());

    }

    //GET request to Dosuku API

    private void getSudoku() {

        try {

            if (requestAmount > 5){

                throw new IOException("The desired sudoku level was not found");

            }

            URI uri = URI.create("https://sudoku-api.vercel.app/api/dosuku");
            URL url = uri.toURL();
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            if(responseCode == HttpsURLConnection.HTTP_OK){

                Scanner input = new Scanner(connection.getInputStream());
                StringBuilder builder = new StringBuilder();

                while (input.hasNext()){

                    builder.append(input.nextLine());

                }

                Gson gson = new Gson();

                Map<String,Object> data = gson.fromJson(builder.toString(), Map.class);

                Map<String,Object> newboard = (Map<String, Object>) data.get("newboard");

                ArrayList<Object> array = (ArrayList<Object>) newboard.get("grids");

                Map<String,Object> sudokuData = (Map<String, Object>) array.get(0);

                System.out.println(sudokuData.get("difficulty"));

                if (!sudokuData.get("difficulty").equals("Hard")){

                    this.requestAmount++;
                    getSudoku();


                }else{

                    this.sudokuData = sudokuData;
                    getSudokuArray("value");
                    getSudokuArray("solution");

                }

            }else {

                throw new IOException("The request was not successful");

            }

        }catch (IOException e){

            System.out.println("There was an error while requesting a new sudoku from the API");
            this.getLocalSudoku();


        }

    }

    private void getSudokuArray(String name){

        //Gson interprets every array inside the json string as ArrayList and every number as Double
        //This code is needed to obtain a normal int[][] matrix to work with

        // Define the type of array you want to deserialize into
        //There is another class called Type in the dependencies so its necessary to specify
        //The constructor of TypeToken is protected, so we use a child anonymous class
        java.lang.reflect.Type arrayType = new TypeToken<int[][]>(){}.getType();

        // Convert ArrayList to JSON string
        Gson gson = new Gson();
        String json = gson.toJson(this.sudokuData.get(name));

        // Deserialize JSON string into a Java multidimensional array
        int[][] originalArray = gson.fromJson(json, arrayType);


        int [] simpleArray = new int[81];
        int k = 0;

        for(int i=0; i<9;i++){

            for (int j=0; j<9; j++){

                simpleArray[k] = originalArray[i][j];
                k++;
            }

        }

        if(name.equals("value")){

            this.sudoku = originalArray;
            this.sudokuArray = simpleArray;

        } else if (name.equals("solution")) {

            this.solution = originalArray;
            this.solutionArray = simpleArray;

        }

    }

    private void getLocalSudoku() {

        try {

            InputStream inputStream = SudokuFetcher.class.getResourceAsStream("/localdata/" + this.difficulty + ".txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder buffer = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            reader.close();

            String text = buffer.toString();

            Gson gson = new Gson();
            JsonArray jsonArray = gson.fromJson(text, JsonArray.class);

            Random random = new Random();
            int index  = random.nextInt(jsonArray.size());

            JsonObject sudokuData = (JsonObject) jsonArray.get(index);

            // Define the type for the map
            Type type = new TypeToken<Map<String, Object>>(){}.getType();

            // Convert the JsonObject to a Map<String, Object>
            Map<String, Object> map = gson.fromJson(sudokuData, type);
            this.sudokuData = map;
            getSudokuArray("value");
            getSudokuArray("solution");


        } catch(IOException e){


            System.out.println("There was an error while reading from local data");

        }

    }

}
