package org.example;

import java.io.File;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

public class MahoutRecommendation {

    public static void main(String[] args) {
        try {
            // Load data.csv from resources using class loader
            URL resource = MahoutRecommendation.class.getClassLoader().getResource("data.csv");
            if (resource == null) {
                throw new IllegalArgumentException("data.csv not found in resources!");
            }

            File dataFile = new File(resource.toURI());
            DataModel model = new FileDataModel(dataFile);

            printUsersAndItems(model);

            UserSimilarity similarity = new EuclideanDistanceSimilarity(model);
            UserNeighborhood neighborhood = new NearestNUserNeighborhood(2, similarity, model);
            Recommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);

            int userID = 4;
            List<RecommendedItem> recommendations = recommender.recommend((long) userID, 3);
            System.out.println("\nRecommended items for User " + userID + ":");

            if (recommendations.isEmpty()) {
                System.out.println("No recommendations found for this user.");
            } else {
                for (RecommendedItem recommendation : recommendations) {
                    System.out.println("Item: " + recommendation.getItemID() + " | Score: " + recommendation.getValue());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printUsersAndItems(DataModel model) throws TasteException {
        System.out.println("Users in dataset:");
        Iterator<Long> users = model.getUserIDs();
        while (users.hasNext()) {
            System.out.println(users.next());
        }

        System.out.println("\nItems in dataset:");
        Iterator<Long> items = model.getItemIDs();
        while (items.hasNext()) {
            System.out.println(items.next());
        }

        System.out.println();
    }
}
