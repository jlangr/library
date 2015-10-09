package domain.core;

import com.loc.material.api.*;

public class MaterialTestData {
   static final String LANGR_YEAR = "2005";
   public static final String LANGR_CLASSIFICATION = "LAN888";
   static final String LANGR_TITLE = "Agile Java";
   static final String LANGR_AUTHOR = "Langr";

   static final String KAFKA_YEAR = "1925";
   static public final String KAFKA_CLASSIFICATION = "KAF666";
   static final String KAFKA_TITLE = "The Trial";
   static final String KAFKA_AUTHOR = "Kafka";

   static final String STRANGELOVE_DIRECTOR = "Kubrick, Stanley";
   static final String STRANGELOVE_CLASSIFICATION = "DAA 0138";
   static final String STRANGELOVE_TITLE = "Dr. Strangelove";
   static final String STRANGELOVE_YEAR = "1992"; // date of video release

   public static final Material THE_TRIAL = new Material(KAFKA_AUTHOR, KAFKA_TITLE,
         KAFKA_CLASSIFICATION, KAFKA_YEAR);
   public static final Material AGILE_JAVA = new Material(LANGR_AUTHOR, LANGR_TITLE,
         LANGR_CLASSIFICATION, LANGR_YEAR);
   public static final Material DR_STRANGELOVE = new Material(STRANGELOVE_DIRECTOR,
         STRANGELOVE_TITLE, STRANGELOVE_CLASSIFICATION, STRANGELOVE_YEAR,
         MaterialType.TYPE_MOVIE);
   public static final Material THE_TRIAL_NEW_EDITION = new Material(KAFKA_AUTHOR,
         KAFKA_TITLE, KAFKA_CLASSIFICATION, KAFKA_YEAR, MaterialType.TYPE_BOOK);
}
