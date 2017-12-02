package cs147.picoachu;

import java.util.Arrays;
import java.util.ArrayList;

/**
 * Hard coded app data
 */
public class Data{

    static int currentUserId = 1;
    static int totalUsers = 3;

    static String challenge1Description = "  A very powerful method of improving the composition of photos is the use of lines. Properly used, lines can significantly increase the impact of images. Lines serve to affect photographic composition.";
    static String challenge2Description = "  Symmetry finds its ways into architecture at every scale, from the overall external views of buildings such as Gothic cathedrals and The White House.";
    static String challenge3Description = "  Symmetry is beautiful. Symmetry appears everywhere in natrue. Think about where these symmetries appear. Some objects can have several symmetries at once, such as both reflectional and rotational symmetry.";
    static String challenge4Description = "  At sunrise and sunset, when the path through the atmosphere is longer, the blue and green components are removed almost completely leaving the longer wavelength orange and red hues seen at those times.";
    static String challenge5Description = "  Cityscapes are lit with a myriad of interesting and colorful light sources, such as lampposts, neon signs, store windows, car lights, and bare bulbs. People dress in their favorite outfits to go out.";
    static String challenge6Description = "  One of the great challenges in photography is to bring a sense of depth to your photos, giving a two-dimensional image a real three-dimensional feel.";
    static String challenge7Description = "  Spacing is always a good thing!";
    static String challenge8Description = "  Color is a powerful weapon to emphasize some objects.";
    // ------------------------------

    static Challenge challenge1 = new Challenge(1, "Abstract Lines and Curves",
            "Abstract", challenge1Description, "examplephoto1");

    static Challenge challenge2 = new Challenge(2, "Symmetry in Building",
            "Symmetry", challenge2Description, "examplephoto2");

    static Challenge challenge3 = new Challenge(3, "Symmetry in Nature",
            "Symmetry", challenge3Description, "examplephoto3");

    static Challenge challenge4 = new Challenge(4, "Lighting at Sunrise or Sunset",
            "Lighting", challenge4Description, "examplephoto4");

    static Challenge challenge5 = new Challenge(5, "Lighting at Night",
            "Lighting", challenge5Description, "examplephoto5");

    static Challenge challenge6 = new Challenge(6, "Depth of Road",
            "Depth", challenge6Description, "examplephoto6");

    static Challenge challenge7 = new Challenge(7, "Emphasis using Spacing",
            "Emphasis", challenge7Description, "examplephoto7");

    static Challenge challenge8 = new Challenge(8, "Emphasis using Color",
            "Emphasis", challenge8Description, "examplephoto8");

    // ------------------------------

    static ArrayList<Challenge> symmetryChallenges = new ArrayList<Challenge>(Arrays.asList(challenge2, challenge3));

    static ArrayList<Challenge> depthChallenges = new ArrayList<Challenge>(Arrays.asList(challenge6));

    static ArrayList<Challenge> brightnessChallenges = new ArrayList<Challenge>(Arrays.asList(challenge4, challenge5));

    static ArrayList<Challenge> lineChallenges = new ArrayList<Challenge>(Arrays.asList(challenge1));

    static ArrayList<Challenge> challenges = new ArrayList<>(Arrays.asList(challenge1, challenge2,
            challenge3, challenge4, challenge5, challenge6, challenge7, challenge8));
    // ------------------------------

    static ArrayList<Integer> user1Accepted = new ArrayList<Integer>(Arrays.asList(1));
    static ArrayList<Integer> user1Completed = new ArrayList<Integer>(Arrays.asList(4, 6));
    static ArrayList<Integer> user1Photos = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
    static User user1 = new User(1, "profilephoto1", "User One", "Palo Alto, CA",
            user1Accepted, user1Completed, user1Photos);

    static ArrayList<Integer> user2Accepted = new ArrayList<Integer>(Arrays.asList(2));
    static ArrayList<Integer> user2Completed = new ArrayList<Integer>(Arrays.asList(5, 8));
    static ArrayList<Integer> user2Photos = new ArrayList<Integer>(Arrays.asList(5, 6));
    static User user2 = new User(2, "profilephoto2", "User Two", "San Francisco, CA",
            user2Accepted, user2Completed, user2Photos);

    static ArrayList<Integer> user3Accepted = new ArrayList<Integer>();
    static ArrayList<Integer> user3Completed = new ArrayList<Integer>(Arrays.asList(7));
    static ArrayList<Integer> user3Photos = new ArrayList<Integer>(Arrays.asList(7, 8, 9, 10));
    static User user3 = new User(3, "profilephoto3", "User Three", "New York, NY",
            user3Accepted, user3Completed, user3Photos);

    // ------------------------------

    static ArrayList<String> photo1Tags = new ArrayList<String>(Arrays.asList(challenge6.title));
    static ArrayList<String> comment1 = new ArrayList<String>(Arrays.asList("User Three: comment one", "849", "511"));
    static ArrayList<String> comment2 = new ArrayList<String>(Arrays.asList("User Two: comment two", "849", "511"));
    static ArrayList<ArrayList<String>> photo1Comments = new ArrayList<ArrayList<String>>(Arrays.asList(comment1, comment2));
    static Photo photo1 = new Photo(1, 1, 6, "userphoto1", photo1Tags, photo1Comments);

    static ArrayList<String> photo2Tags = new ArrayList<String>(Arrays.asList("nature"));
    static ArrayList<ArrayList<String>> photo2Comments = new ArrayList<ArrayList<String>>();
    static Photo photo2 = new Photo(2, 1, 0, "userphoto2", photo2Tags, photo2Comments);

    static ArrayList<String> photo3Tags = new ArrayList<String>(Arrays.asList("nature"));
    static ArrayList<ArrayList<String>> photo3Comments = new ArrayList<ArrayList<String>>();
    static Photo photo3 = new Photo(3, 1, 0, "userphoto3", photo3Tags, photo3Comments);

    static ArrayList<String> photo4Tags = new ArrayList<String>(Arrays.asList(challenge4.title, "nature", "depth"));

    static ArrayList<ArrayList<String>> photo4Comments = new ArrayList<ArrayList<String>>();
    static Photo photo4 = new Photo(4, 1, 4, "userphoto4", photo4Tags, photo4Comments);

    static ArrayList<String> photo5Tags = new ArrayList<String>(Arrays.asList(challenge5.title));
    static ArrayList<ArrayList<String>> photo5Comments = new ArrayList<ArrayList<String>>();
    static Photo photo5 = new Photo(5, 2, 5, "userphoto5", photo5Tags, photo5Comments);

    static ArrayList<String> photo6Tags = new ArrayList<String>(Arrays.asList(challenge8.title, "depth"));
    static ArrayList<ArrayList<String>> photo6Comments = new ArrayList<ArrayList<String>>();
    static Photo photo6 = new Photo(6, 2, 8, "userphoto6", photo6Tags, photo6Comments);

    static ArrayList<String> photo7Tags = new ArrayList<String>(Arrays.asList("cat"));
    static ArrayList<ArrayList<String>> photo7Comments = new ArrayList<ArrayList<String>>();
    static Photo photo7 = new Photo(7, 3, 0, "userphoto7", photo7Tags, photo7Comments);

    static ArrayList<String> photo8Tags = new ArrayList<String>(Arrays.asList("cat"));
    static ArrayList<ArrayList<String>> photo8Comments = new ArrayList<ArrayList<String>>();
    static Photo photo8 = new Photo(8, 3, 0, "userphoto8", photo8Tags, photo8Comments);

    static ArrayList<String> photo9Tags = new ArrayList<String>(Arrays.asList("cat"));
    static ArrayList<ArrayList<String>> photo9Comments = new ArrayList<ArrayList<String>>();
    static Photo photo9 = new Photo(9, 3, 0, "userphoto9", photo9Tags, photo9Comments);

    static ArrayList<String> photo10Tags = new ArrayList<String>(Arrays.asList(challenge7.title));
    static ArrayList<ArrayList<String>> photo10Comments = new ArrayList<ArrayList<String>>();
    static Photo photo10 = new Photo(10, 3, 7, "userphoto10", photo10Tags, photo10Comments);

    // ------------------------------

    public static void nextUser(){
        currentUserId = (currentUserId % totalUsers) + 1;
    }

    public static User getUser(int id){
        switch (id){
            case 1:
                return user1;
            case 2:
                return user2;
            case 3:
                return user3;
            default:
                return null;
        }
    }

    public static Challenge getChallenge(int id){
        switch (id){
            case 1:
                return challenge1;
            case 2:
                return challenge2;
            case 3:
                return challenge3;
            case 4:
                return challenge4;
            case 5:
                return challenge5;
            case 6:
                return challenge6;
            case 7:
                return challenge7;
            case 8:
                return challenge8;
            default:
                return null;
        }
    }

    public static Photo getPhoto(int id){
        switch (id){
            case 1:
                return photo1;
            case 2:
                return photo2;
            case 3:
                return photo3;
            case 4:
                return photo4;
            case 5:
                return photo5;
            case 6:
                return photo6;
            case 7:
                return photo7;
            case 8:
                return photo8;
            case 9:
                return photo9;
            case 10:
                return photo10;
            default:
                return null;
        }
    }


}

class User {

    public int userid;

    public String profilePhotoName;

    public String userName;

    public String location;

    public ArrayList<Integer> acceptedChallenges;

    public ArrayList<Integer> completedChallenges;

    public ArrayList<Integer> photos;

    public User(int _userid, String _profilePhotoName, String _userName, String _location, ArrayList<Integer> _acceptedChallenges,
                ArrayList<Integer> _completedChallenges, ArrayList<Integer> _photos){
        userid = _userid;
        profilePhotoName = _profilePhotoName;
        userName = _userName;
        location = _location;
        acceptedChallenges = _acceptedChallenges;
        completedChallenges = _completedChallenges;
        photos = _photos;
    }
}

class Photo {

    public int photoid;

    public int ownerid;

    public int forChallenge;

    public String userPhotoName;

    public ArrayList<String> tags;

    public ArrayList<ArrayList<String>> comments;

    public Photo(int _photoid, int _ownerid, int _forChallenge, String _userPhotoName, ArrayList<String> _tags, ArrayList<ArrayList<String>> _comments){
        photoid = _photoid;
        ownerid = _ownerid;
        forChallenge = _forChallenge;
        userPhotoName = _userPhotoName;
        tags = _tags;
        comments = _comments;
    }
}

class Challenge {

    public int challengeid;

    public String title;

    public String topic;

    public String description;

    public String examplePhotoName;

    public Challenge(int _challengeid, String _title, String _topic, String _description, String _examplePhotoName){
        challengeid = _challengeid;
        title = _title;
        topic = _topic;
        description = _description;
        examplePhotoName = _examplePhotoName;
    }
}