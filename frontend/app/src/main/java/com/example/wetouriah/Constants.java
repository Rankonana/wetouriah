package com.example.wetouriah;

import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Constants {
    public static final String SERVER_IP_ADDRESS = "wetouriah.careercrest.co.za"; // Replace with your actual IP address
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";


    private static final int TRACKING_NUMBER_LENGTH = 10;

    public static String formatDateString(String inputDateString) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSX", Locale.US);
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale.US);

            Date date = inputFormat.parse(inputDateString);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return ""; // Handle parsing error as needed
        }
    }


    public static String ChangeDateToISO(String inputDate) {
        try {
            // Define the input and output date formats
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd MMM yyyy");
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

            // Parse the input date string
            Date date = inputFormat.parse(inputDate);

            // Format the date to the desired output format
            String outputDate = outputFormat.format(date);

            return outputDate;
        } catch (ParseException e) {
            // Handle parsing exceptions if the input date is not in the expected format
            e.printStackTrace();
            return null; // Return null or another error indicator if needed
        }
    }



    public static String formatTrackingLogDateString(String inputDateString) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSX", Locale.US);
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM, HH:mm", Locale.US);

            Date date = inputFormat.parse(inputDateString);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return ""; // Handle parsing error as needed
        }
    }

    public static String formatDate(String inputDate) {
        try {
            SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
            SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.US);

            // Parse the input date string
            Date date = inputDateFormat.parse(inputDate);

            // Format the date into the desired format
            String formattedDate = outputDateFormat.format(date);

            return formattedDate;
        } catch (ParseException e) {
            e.printStackTrace();
            return ""; // Handle the parsing error if necessary
        }
    }

    public static String reverseDate(String inputDate) {
        try {
            SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
            SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

            // Parse the input date string
            Date date = inputDateFormat.parse(inputDate);

            // Format the date into the desired format
            String isoDate = outputDateFormat.format(date);

            return isoDate;
        } catch (ParseException e) {
            e.printStackTrace();
            return ""; // Handle the parsing error if necessary
        }
    }
    public static String generateTrackingNumber() {
        SecureRandom random = new SecureRandom();
        StringBuilder trackingNumber = new StringBuilder();

        for (int i = 0; i < TRACKING_NUMBER_LENGTH; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            trackingNumber.append(randomChar);
        }

        return trackingNumber.toString();
    }


}
