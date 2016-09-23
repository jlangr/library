package domain.core;

import com.loc.material.api.*;

public class MaterialTestData {
   static final String LANGR_ISBN = "9780131482395";
   static final String LANGR_YEAR = "2005";
   public static final String LANGR_CLASSIFICATION = "LAN888";
   static final String LANGR_TITLE = "Agile Java";
   static final String LANGR_AUTHOR = "Langr";

   static final String KAFKA_NEW_ISBN = "9780805209990";

   static final String KAFKA_ISBN = "9781612931036";
   static final String KAFKA_YEAR = "1925";
   public static final String KAFKA_CLASSIFICATION = "KAF666";
   static final String KAFKA_TITLE = "The Trial";
   static final String KAFKA_AUTHOR = "Kafka";

   static final String STRANGELOVE_ID = "";
   static final String STRANGELOVE_DIRECTOR = "Kubrick, Stanley";
   static final String STRANGELOVE_CLASSIFICATION = "DAA 0138";
   static final String STRANGELOVE_TITLE = "Dr. Strangelove";
   static final String STRANGELOVE_YEAR = "1992"; // date of video release

   public static final MaterialDetails THE_TRIAL = new MaterialDetails(KAFKA_ISBN, KAFKA_AUTHOR, KAFKA_TITLE,
      KAFKA_CLASSIFICATION, KAFKA_YEAR);
   public static final MaterialDetails AGILE_JAVA = new MaterialDetails(LANGR_ISBN, LANGR_AUTHOR, LANGR_TITLE,
      LANGR_CLASSIFICATION, LANGR_YEAR);
   public static final MaterialDetails DR_STRANGELOVE = new MaterialDetails(STRANGELOVE_ID,
      STRANGELOVE_DIRECTOR, STRANGELOVE_TITLE, STRANGELOVE_CLASSIFICATION, MaterialType.DVD,
      STRANGELOVE_YEAR);
   public static final MaterialDetails THE_TRIAL_NEW_EDITION = new MaterialDetails(KAFKA_NEW_ISBN,
      KAFKA_AUTHOR, KAFKA_TITLE, KAFKA_CLASSIFICATION, MaterialType.Book, KAFKA_YEAR);
}
