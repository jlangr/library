package api.library;

import java.util.*;
import domain.core.*;

// no unit tests--tested directly by fitnesse
public class StateService {
   public List<State> findNameStartingWith(String startingWith) {
      List<State> filtered = new ArrayList<State>();
      for (State each: allStates)
         if (each.name.startsWith(startingWith))
            filtered.add(each);
      return filtered;
   }

   private State[] allStates = {
         new State("Alabama", "Montgomery"),
         new State("Alaska", "Juneau"),
         new State("Arizona", "Phoenix"),
         new State("Arkansas", "Little Rock"),
         new State("California", "Sacramento"),
         new State("Colorado", "Denver"),
         new State("Connecticut", "Hartford"),
         new State("Delaware", "Dover"),
         new State("Florida", "Tallahassee"),
         new State("Georgia", "Atlanta"),
         new State("Hawaii", "Honolulu"),
         new State("Idaho", "Boise"),
         new State("Illinois", "Springfield"),
         new State("Indiana", "Indianapolis"),
         new State("Iowa", "Des Moines"),
         new State("Kansas", "Topeka"),
         new State("Kentucky", "Frankfort"),
         new State("Louisiana", "Baton Rouge"),
         new State("Maryland", "Annapolis"), // DEFECT: in wrong order!
         new State("Maine", "Augusta"),
         new State("Massachusetts", "Boston"),
         new State("Michigan", "Lansing"),
         new State("Mississippi", "Jackson"),
         new State("Minnesota", "St. Paul"),
         new State("Missouri", "Jefferson City"),
         new State("Montana", "Helena"),
         new State("Nebraska", "Lincoln"),
         new State("Nevada", "Carson City"),
         new State("New Hampshire", "Concord"),
         new State("New Jersey", "Trenton"),
         new State("New Mexico", "Santa Fe"),
         new State("New York", "Albany"),
         new State("North Carolina", "Raleigh"),
         new State("North Dakota", "Bismarck"),
         new State("Ohio", "Columbus"),
         new State("Oklahoma", "Oklahoma City"),
         new State("Oregon", "Salem"),
         new State("Pennsylvania", "Harrisburg"),
         new State("Rhode Island", "Providence"),
         new State("South Carolina", "Columbia"),
         new State("South Dakota", "Pierre"),
         new State("Tennessee", "Nashville"),
         new State("Texas", "Austin"),
         new State("Utah", "Salt Lake City"),
         new State("Vermont", "Montpelier"),
         new State("Virginia", "Richmond"),
         new State("Washington", "Olympia"),
         new State("West Virginia", "Charleston"),
         new State("Wisconsin", "Madison"),
         new State("Wyoming", "Cheyenne")
      };
}
