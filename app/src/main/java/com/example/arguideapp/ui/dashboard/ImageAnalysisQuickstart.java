package com.example.arguideapp.ui.dashboard;

import com.microsoft.azure.cognitiveservices.vision.computervision.ComputerVisionClient;
import com.microsoft.azure.cognitiveservices.vision.computervision.ComputerVisionManager;
import com.microsoft.azure.cognitiveservices.vision.computervision.models.Category;
import com.microsoft.azure.cognitiveservices.vision.computervision.models.ImageAnalysis;
import com.microsoft.azure.cognitiveservices.vision.computervision.models.LandmarksModel;
import com.microsoft.azure.cognitiveservices.vision.computervision.models.VisualFeatureTypes;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

public class ImageAnalysisQuickstart {
    static String key = "bd9996f36a214fbaaf20cefaf0009d28";
    static String endpoint = "https://ar-guide-app-computer-vision.cognitiveservices.azure.com/";

    private static String currentRequest;


    public ImageAnalysisQuickstart() {
    }

    public static void main() {
        ComputerVisionClient compVisClient = Authenticate(key, endpoint);
        AnalyzeRemoteImage(compVisClient);
    }

    public static ComputerVisionClient Authenticate(String key, String endpoint) {
        return ComputerVisionManager.authenticate(key).withEndpoint(endpoint);
    }

    public static String getCurrentRequest() {
        return currentRequest;
    }


    public static void AnalyzeRemoteImage(ComputerVisionClient compVisClient) {
        String pathToRemoteImage = DashboardFragment.getCurrentLink();
        List<VisualFeatureTypes> featuresToExtractFromRemoteImage = new ArrayList();
        featuresToExtractFromRemoteImage.add(VisualFeatureTypes.BRANDS);
        featuresToExtractFromRemoteImage.add(VisualFeatureTypes.DESCRIPTION);
        featuresToExtractFromRemoteImage.add(VisualFeatureTypes.CATEGORIES);
        featuresToExtractFromRemoteImage.add(VisualFeatureTypes.TAGS);
        featuresToExtractFromRemoteImage.add(VisualFeatureTypes.FACES);
        featuresToExtractFromRemoteImage.add(VisualFeatureTypes.ADULT);
        featuresToExtractFromRemoteImage.add(VisualFeatureTypes.COLOR);
        featuresToExtractFromRemoteImage.add(VisualFeatureTypes.IMAGE_TYPE);

        try {
            ImageAnalysis analysis = compVisClient.computerVision().analyzeImage().withUrl(pathToRemoteImage).withVisualFeatures(featuresToExtractFromRemoteImage).execute();
            Dictionary landmarks = new Hashtable();
            Iterator var5 = analysis.categories().iterator();

            while(true) {
                Category category;
                do {
                    do {
                        if (!var5.hasNext()) {
                            String res = String.valueOf(((Hashtable)landmarks).keySet());
                            String charsToRemove = "[]";
                            char[] var14 = charsToRemove.toCharArray();
                            int var15 = var14.length;

                            for(int var9 = 0; var9 < var15; ++var9) {
                                char c = var14[var9];
                                res = res.replace(String.valueOf(c), "");
                            }

                            //TODO:System.out.println(res);
                            currentRequest = res;
                            return;
                        }

                        category = (Category)var5.next();
                    } while(category.detail() == null);
                } while(category.detail().landmarks() == null);

                Iterator var7 = category.detail().landmarks().iterator();

                while(var7.hasNext()) {
                    LandmarksModel landmark = (LandmarksModel)var7.next();
                    if (((Hashtable)landmarks).containsKey(landmark.name()) && ((Hashtable)landmarks).containsValue(landmark.name())) {
                        break;
                    }

                    landmarks.put(landmark.name(), landmark.confidence());
                }
            }
        } catch (Exception var11) {
            //System.out.println(var11.getMessage());
            var11.printStackTrace();
        }
    }
}
