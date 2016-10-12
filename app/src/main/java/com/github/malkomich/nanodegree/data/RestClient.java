package com.github.malkomich.nanodegree.data;

import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Generic REST client to retrieve data from web services.
 */
public abstract class RestClient {

    private String baseUri;
    protected JSONObject data;

    protected RestClient(String baseUri) {
        this.baseUri = baseUri;
    }

    /**
     * Check if domain objects has been set.
     *
     * @return Success status
     */
    protected boolean apiSuccess() {
        return data != null;
    };

    /**
     * Joins the base url of the web service with the required params.
     *
     * @param resource
     *              Resource of the web service which is being requested
     * @param paramNames
     *              Names of the params sent to build the URL
     * @param paramValues
     *              Values of the params sent to build the URL
     * @return Service URL
     */
    protected URL buildURL(String resource, String[] paramNames, String[] paramValues, int numParams) {

        String baseUri = resource != null ? this.baseUri + "/" + resource : this.baseUri;
        baseUri += numParams > 0 ? "?" : "";

        Uri.Builder uriBuilder = Uri.parse(baseUri)
            .buildUpon();
        for(int i=0; i < numParams; i++) {
            uriBuilder.appendQueryParameter(paramNames[i], paramValues[i]);
        }

        try {
            return new URL(uriBuilder.build()
                .toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Calls the REST API to retrieve the String data, and then calls to the
     * parser.
     *
     * @param url
     *            Service URL
     */
    protected void doRequest(URL url) {

//        StringBuilder output = new StringBuilder();
//
//        try {
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("GET");
//            conn.setRequestProperty("Accept", "application/json");
//
//            if (conn.getResponseCode() != 200) {
//                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
//            }
//
//            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
//
//            String line;
//            while ((line = br.readLine()) != null) {
//                output.append(line);
//            }
//
//            conn.disconnect();
//
//        } catch (IOException e) {
//
//            e.printStackTrace();
//
//        }
//
//        parseOutput(output.toString());

        parseOutput("{\n" +
            "  \"page\": 1,\n" +
            "  \"results\": [\n" +
            "    {\n" +
            "      \"poster_path\": \"/z6BP8yLwck8mN9dtdYKkZ4XGa3D.jpg\",\n" +
            "      \"adult\": false,\n" +
            "      \"overview\": \"A big screen remake of John Sturges' classic western The Magnificent Seven, itself" +
            " a remake of Akira Kurosawa's Seven Samurai. Seven gun men in the old west gradually come together to " +
            "help a poor village against savage thieves.\",\n" +
            "      \"release_date\": \"2016-09-14\",\n" +
            "      \"genre_ids\": [\n" +
            "        28,\n" +
            "        12,\n" +
            "        37\n" +
            "      ],\n" +
            "      \"id\": 333484,\n" +
            "      \"original_title\": \"The Magnificent Seven\",\n" +
            "      \"original_language\": \"en\",\n" +
            "      \"title\": \"The Magnificent Seven\",\n" +
            "      \"backdrop_path\": \"/T3LrH6bnV74llVbFpQsCBrGaU9.jpg\",\n" +
            "      \"popularity\": 34.09003,\n" +
            "      \"vote_count\": 468,\n" +
            "      \"video\": false,\n" +
            "      \"vote_average\": 4.56\n" +
            "    },\n" +
            "    {\n" +
            "      \"poster_path\": \"/5N20rQURev5CNDcMjHVUZhpoCNC.jpg\",\n" +
            "      \"adult\": false,\n" +
            "      \"overview\": \"Following the events of Age of Ultron, the collective governments of the world " +
            "pass an act designed to regulate all superhuman activity. This polarizes opinion amongst the Avengers, " +
            "causing two factions to side with Iron Man or Captain America, which causes an epic battle between " +
            "former allies.\",\n" +
            "      \"release_date\": \"2016-04-27\",\n" +
            "      \"genre_ids\": [\n" +
            "        28,\n" +
            "        878,\n" +
            "        53\n" +
            "      ],\n" +
            "      \"id\": 271110,\n" +
            "      \"original_title\": \"Captain America: Civil War\",\n" +
            "      \"original_language\": \"en\",\n" +
            "      \"title\": \"Captain America: Civil War\",\n" +
            "      \"backdrop_path\": \"/m5O3SZvQ6EgD5XXXLPIP1wLppeW.jpg\",\n" +
            "      \"popularity\": 25.850447,\n" +
            "      \"vote_count\": 3172,\n" +
            "      \"video\": false,\n" +
            "      \"vote_average\": 6.77\n" +
            "    },\n" +
            "    {\n" +
            "      \"poster_path\": \"/uSHjeRVuObwdpbECaXJnvyDoeJK.jpg\",\n" +
            "      \"adult\": false,\n" +
            "      \"overview\": \"A teenager finds himself transported to an island where he must help protect a " +
            "group of orphans with special powers from creatures intent on destroying them.\",\n" +
            "      \"release_date\": \"2016-09-28\",\n" +
            "      \"genre_ids\": [\n" +
            "        14\n" +
            "      ],\n" +
            "      \"id\": 283366,\n" +
            "      \"original_title\": \"Miss Peregrine's Home for Peculiar Children\",\n" +
            "      \"original_language\": \"en\",\n" +
            "      \"title\": \"Miss Peregrine's Home for Peculiar Children\",\n" +
            "      \"backdrop_path\": \"/qXQinDhDZkTiqEGLnav0h1YSUu8.jpg\",\n" +
            "      \"popularity\": 24.447453,\n" +
            "      \"vote_count\": 143,\n" +
            "      \"video\": false,\n" +
            "      \"vote_average\": 6.37\n" +
            "    },\n" +
            "    {\n" +
            "      \"poster_path\": \"/e1mjopzAS2KNsvpbpahQ1a6SkSn.jpg\",\n" +
            "      \"adult\": false,\n" +
            "      \"overview\": \"From DC Comics comes the Suicide Squad, an antihero team of incarcerated " +
            "supervillains who act as deniable assets for the United States government, undertaking high-risk black " +
            "ops missions in exchange for commuted prison sentences.\",\n" +
            "      \"release_date\": \"2016-08-03\",\n" +
            "      \"genre_ids\": [\n" +
            "        28,\n" +
            "        80,\n" +
            "        14\n" +
            "      ],\n" +
            "      \"id\": 297761,\n" +
            "      \"original_title\": \"Suicide Squad\",\n" +
            "      \"original_language\": \"en\",\n" +
            "      \"title\": \"Suicide Squad\",\n" +
            "      \"backdrop_path\": \"/ndlQ2Cuc3cjTL7lTynw6I4boP4S.jpg\",\n" +
            "      \"popularity\": 20.082982,\n" +
            "      \"vote_count\": 2075,\n" +
            "      \"video\": false,\n" +
            "      \"vote_average\": 5.94\n" +
            "    },\n" +
            "    {\n" +
            "      \"poster_path\": \"/9KQX22BeFzuNM66pBA6JbiaJ7Mi.jpg\",\n" +
            "      \"adult\": false,\n" +
            "      \"overview\": \"We always knew they were coming back. Using recovered alien technology, the " +
            "nations of Earth have collaborated on an immense defense program to protect the planet. But nothing can " +
            "prepare us for the aliens’ advanced and unprecedented force. Only the ingenuity of a few brave men and " +
            "women can bring our world back from the brink of extinction.\",\n" +
            "      \"release_date\": \"2016-06-22\",\n" +
            "      \"genre_ids\": [\n" +
            "        28,\n" +
            "        12,\n" +
            "        878\n" +
            "      ],\n" +
            "      \"id\": 47933,\n" +
            "      \"original_title\": \"Independence Day: Resurgence\",\n" +
            "      \"original_language\": \"en\",\n" +
            "      \"title\": \"Independence Day: Resurgence\",\n" +
            "      \"backdrop_path\": \"/8SqBiesvo1rh9P1hbJTmnVum6jv.jpg\",\n" +
            "      \"popularity\": 19.262199,\n" +
            "      \"vote_count\": 1107,\n" +
            "      \"video\": false,\n" +
            "      \"vote_average\": 4.74\n" +
            "    },\n" +
            "    {\n" +
            "      \"poster_path\": \"/7D6hM7IR0TbQmNvSZVtEiPM3H5h.jpg\",\n" +
            "      \"adult\": false,\n" +
            "      \"overview\": \"A story set on the offshore drilling rig Deepwater Horizon, which exploded during " +
            "April 2010 and created the worst oil spill in U.S. history.\",\n" +
            "      \"release_date\": \"2016-09-28\",\n" +
            "      \"genre_ids\": [\n" +
            "        18\n" +
            "      ],\n" +
            "      \"id\": 296524,\n" +
            "      \"original_title\": \"Deepwater Horizon\",\n" +
            "      \"original_language\": \"en\",\n" +
            "      \"title\": \"Deepwater Horizon\",\n" +
            "      \"backdrop_path\": \"/zjYdnBHbIOYBqKZxvBUsT5MevUA.jpg\",\n" +
            "      \"popularity\": 17.800883,\n" +
            "      \"vote_count\": 159,\n" +
            "      \"video\": false,\n" +
            "      \"vote_average\": 4.28\n" +
            "    },\n" +
            "    {\n" +
            "      \"poster_path\": \"/zSouWWrySXshPCT4t3UKCQGayyo.jpg\",\n" +
            "      \"adult\": false,\n" +
            "      \"overview\": \"After the re-emergence of the world's first mutant, world-destroyer Apocalypse, " +
            "the X-Men must unite to defeat his extinction level plan.\",\n" +
            "      \"release_date\": \"2016-05-18\",\n" +
            "      \"genre_ids\": [\n" +
            "        28,\n" +
            "        12,\n" +
            "        14,\n" +
            "        878\n" +
            "      ],\n" +
            "      \"id\": 246655,\n" +
            "      \"original_title\": \"X-Men: Apocalypse\",\n" +
            "      \"original_language\": \"en\",\n" +
            "      \"title\": \"X-Men: Apocalypse\",\n" +
            "      \"backdrop_path\": \"/oQWWth5AOtbWG9o8SCAviGcADed.jpg\",\n" +
            "      \"popularity\": 16.951691,\n" +
            "      \"vote_count\": 2014,\n" +
            "      \"video\": false,\n" +
            "      \"vote_average\": 6.07\n" +
            "    },\n" +
            "    {\n" +
            "      \"poster_path\": \"/4qnJ1hsMADxzwnOmnwjZTNp0rKT.jpg\",\n" +
            "      \"adult\": false,\n" +
            "      \"overview\": \"Following a ghost invasion of Manhattan, paranormal enthusiasts Erin Gilbert and " +
            "Abby Yates, nuclear engineer Jillian Holtzmann, and subway worker Patty Tolan band together to stop the " +
            "otherworldly threat.\",\n" +
            "      \"release_date\": \"2016-07-14\",\n" +
            "      \"genre_ids\": [\n" +
            "        28,\n" +
            "        35,\n" +
            "        14\n" +
            "      ],\n" +
            "      \"id\": 43074,\n" +
            "      \"original_title\": \"Ghostbusters\",\n" +
            "      \"original_language\": \"en\",\n" +
            "      \"title\": \"Ghostbusters\",\n" +
            "      \"backdrop_path\": \"/lbtG9MTb8i13KhXuyl3bmnqt7Lt.jpg\",\n" +
            "      \"popularity\": 16.72339,\n" +
            "      \"vote_count\": 629,\n" +
            "      \"video\": false,\n" +
            "      \"vote_average\": 5.23\n" +
            "    },\n" +
            "    {\n" +
            "      \"poster_path\": \"/1ZQVHkvOegv5wVzxD2fphcxl1Ba.jpg\",\n" +
            "      \"adult\": false,\n" +
            "      \"overview\": \"Set after the events of Continental Drift, Scrat's epic pursuit of his elusive " +
            "acorn catapults him outside of Earth, where he accidentally sets off a series of cosmic events that " +
            "transform and threaten the planet. To save themselves from peril, Manny, Sid, Diego, and the rest of the" +
            " herd leave their home and embark on a quest full of thrills and spills, highs and lows, laughter and " +
            "adventure while traveling to exotic new lands and encountering a host of colorful new characters.\",\n" +
            "      \"release_date\": \"2016-06-23\",\n" +
            "      \"genre_ids\": [\n" +
            "        35,\n" +
            "        16,\n" +
            "        12,\n" +
            "        10751,\n" +
            "        878\n" +
            "      ],\n" +
            "      \"id\": 278154,\n" +
            "      \"original_title\": \"Ice Age: Collision Course\",\n" +
            "      \"original_language\": \"en\",\n" +
            "      \"title\": \"Ice Age: Collision Course\",\n" +
            "      \"backdrop_path\": \"/o29BFNqgXOUT1yHNYusnITsH7P9.jpg\",\n" +
            "      \"popularity\": 15.952381,\n" +
            "      \"vote_count\": 321,\n" +
            "      \"video\": false,\n" +
            "      \"vote_average\": 5.3\n" +
            "    },\n" +
            "    {\n" +
            "      \"poster_path\": \"/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg\",\n" +
            "      \"adult\": false,\n" +
            "      \"overview\": \"Interstellar chronicles the adventures of a group of explorers who make use of a " +
            "newly discovered wormhole to surpass the limitations on human space travel and conquer the vast " +
            "distances involved in an interstellar voyage.\",\n" +
            "      \"release_date\": \"2014-11-05\",\n" +
            "      \"genre_ids\": [\n" +
            "        12,\n" +
            "        18,\n" +
            "        878\n" +
            "      ],\n" +
            "      \"id\": 157336,\n" +
            "      \"original_title\": \"Interstellar\",\n" +
            "      \"original_language\": \"en\",\n" +
            "      \"title\": \"Interstellar\",\n" +
            "      \"backdrop_path\": \"/xu9zaAevzQ5nnrsXN6JcahLnG4i.jpg\",\n" +
            "      \"popularity\": 15.719975,\n" +
            "      \"vote_count\": 5814,\n" +
            "      \"video\": false,\n" +
            "      \"vote_average\": 8.1\n" +
            "    },\n" +
            "    {\n" +
            "      \"poster_path\": \"/ghL4ub6vwbYShlqCFHpoIRwx2sm.jpg\",\n" +
            "      \"adult\": false,\n" +
            "      \"overview\": \"The USS Enterprise crew explores the furthest reaches of uncharted space, where " +
            "they encounter a mysterious new enemy who puts them and everything the Federation stands for to the test" +
            ".\",\n" +
            "      \"release_date\": \"2016-07-07\",\n" +
            "      \"genre_ids\": [\n" +
            "        28,\n" +
            "        12,\n" +
            "        878,\n" +
            "        53\n" +
            "      ],\n" +
            "      \"id\": 188927,\n" +
            "      \"original_title\": \"Star Trek Beyond\",\n" +
            "      \"original_language\": \"en\",\n" +
            "      \"title\": \"Star Trek Beyond\",\n" +
            "      \"backdrop_path\": \"/mGraqqKJIHe7AzQF1qOseD9BcZx.jpg\",\n" +
            "      \"popularity\": 15.241558,\n" +
            "      \"vote_count\": 789,\n" +
            "      \"video\": false,\n" +
            "      \"vote_average\": 6.37\n" +
            "    },\n" +
            "    {\n" +
            "      \"poster_path\": \"/jjBgi2r5cRt36xF6iNUEhzscEcb.jpg\",\n" +
            "      \"adult\": false,\n" +
            "      \"overview\": \"Twenty-two years after the events of Jurassic Park, Isla Nublar now features a " +
            "fully functioning dinosaur theme park, Jurassic World, as originally envisioned by John Hammond.\",\n" +
            "      \"release_date\": \"2015-06-09\",\n" +
            "      \"genre_ids\": [\n" +
            "        28,\n" +
            "        12,\n" +
            "        878,\n" +
            "        53\n" +
            "      ],\n" +
            "      \"id\": 135397,\n" +
            "      \"original_title\": \"Jurassic World\",\n" +
            "      \"original_language\": \"en\",\n" +
            "      \"title\": \"Jurassic World\",\n" +
            "      \"backdrop_path\": \"/dkMD5qlogeRMiEixC4YNPUvax2T.jpg\",\n" +
            "      \"popularity\": 14.967361,\n" +
            "      \"vote_count\": 5099,\n" +
            "      \"video\": false,\n" +
            "      \"vote_average\": 6.59\n" +
            "    },\n" +
            "    {\n" +
            "      \"poster_path\": \"/7rfucKCjV01OEsGr9grXa34Aywd.jpg\",\n" +
            "      \"adult\": false,\n" +
            "      \"overview\": \"In the sequel to Tim Burton's \\\"Alice in Wonderland\\\", Alice Kingsleigh " +
            "returns to Underland and faces a new adventure in saving the Mad Hatter.\",\n" +
            "      \"release_date\": \"2016-05-25\",\n" +
            "      \"genre_ids\": [\n" +
            "        12,\n" +
            "        10751,\n" +
            "        14\n" +
            "      ],\n" +
            "      \"id\": 241259,\n" +
            "      \"original_title\": \"Alice Through the Looking Glass\",\n" +
            "      \"original_language\": \"en\",\n" +
            "      \"title\": \"Alice Through the Looking Glass\",\n" +
            "      \"backdrop_path\": \"/rWlXrfmX1FgcPyj7oQmLfwKRaam.jpg\",\n" +
            "      \"popularity\": 14.465188,\n" +
            "      \"vote_count\": 430,\n" +
            "      \"video\": false,\n" +
            "      \"vote_average\": 6.43\n" +
            "    },\n" +
            "    {\n" +
            "      \"poster_path\": \"/ckrTPz6FZ35L5ybjqvkLWzzSLO7.jpg\",\n" +
            "      \"adult\": false,\n" +
            "      \"overview\": \"The peaceful realm of Azeroth stands on the brink of war as its civilization faces" +
            " a fearsome race of invaders: orc warriors fleeing their dying home to colonize another. As a portal " +
            "opens to connect the two worlds, one army faces destruction and the other faces extinction. From " +
            "opposing sides, two heroes are set on a collision course that will decide the fate of their family, " +
            "their people, and their home.\",\n" +
            "      \"release_date\": \"2016-05-25\",\n" +
            "      \"genre_ids\": [\n" +
            "        28,\n" +
            "        12,\n" +
            "        14\n" +
            "      ],\n" +
            "      \"id\": 68735,\n" +
            "      \"original_title\": \"Warcraft\",\n" +
            "      \"original_language\": \"en\",\n" +
            "      \"title\": \"Warcraft\",\n" +
            "      \"backdrop_path\": \"/5SX2rgKXZ7NVmAJR5z5LprqSXKa.jpg\",\n" +
            "      \"popularity\": 14.2977,\n" +
            "      \"vote_count\": 926,\n" +
            "      \"video\": false,\n" +
            "      \"vote_average\": 6.07\n" +
            "    },\n" +
            "    {\n" +
            "      \"poster_path\": \"/kqjL17yufvn9OVLyXYpvtyrFfak.jpg\",\n" +
            "      \"adult\": false,\n" +
            "      \"overview\": \"An apocalyptic story set in the furthest reaches of our planet, in a stark desert " +
            "landscape where humanity is broken, and most everyone is crazed fighting for the necessities of life. " +
            "Within this world exist two rebels on the run who just might be able to restore order. There's Max, a " +
            "man of action and a man of few words, who seeks peace of mind following the loss of his wife and child " +
            "in the aftermath of the chaos. And Furiosa, a woman of action and a woman who believes her path to " +
            "survival may be achieved if she can make it across the desert back to her childhood homeland.\",\n" +
            "      \"release_date\": \"2015-05-13\",\n" +
            "      \"genre_ids\": [\n" +
            "        28,\n" +
            "        12,\n" +
            "        878,\n" +
            "        53\n" +
            "      ],\n" +
            "      \"id\": 76341,\n" +
            "      \"original_title\": \"Mad Max: Fury Road\",\n" +
            "      \"original_language\": \"en\",\n" +
            "      \"title\": \"Mad Max: Fury Road\",\n" +
            "      \"backdrop_path\": \"/tbhdm8UJAb4ViCTsulYFL3lxMCd.jpg\",\n" +
            "      \"popularity\": 12.82354,\n" +
            "      \"vote_count\": 5476,\n" +
            "      \"video\": false,\n" +
            "      \"vote_average\": 7.23\n" +
            "    },\n" +
            "    {\n" +
            "      \"poster_path\": \"/6FxOPJ9Ysilpq0IgkrMJ7PubFhq.jpg\",\n" +
            "      \"adult\": false,\n" +
            "      \"overview\": \"Tarzan, having acclimated to life in London, is called back to his former home in " +
            "the jungle to investigate the activities at a mining encampment.\",\n" +
            "      \"release_date\": \"2016-06-29\",\n" +
            "      \"genre_ids\": [\n" +
            "        28,\n" +
            "        12\n" +
            "      ],\n" +
            "      \"id\": 258489,\n" +
            "      \"original_title\": \"The Legend of Tarzan\",\n" +
            "      \"original_language\": \"en\",\n" +
            "      \"title\": \"The Legend of Tarzan\",\n" +
            "      \"backdrop_path\": \"/uC8RkYALi9VWHeZQWLRUqjxfW09.jpg\",\n" +
            "      \"popularity\": 12.805684,\n" +
            "      \"vote_count\": 1191,\n" +
            "      \"video\": false,\n" +
            "      \"vote_average\": 4.96\n" +
            "    },\n" +
            "    {\n" +
            "      \"poster_path\": \"/inVq3FRqcYIRl2la8iZikYYxFNR.jpg\",\n" +
            "      \"adult\": false,\n" +
            "      \"overview\": \"Based upon Marvel Comics’ most unconventional anti-hero, DEADPOOL tells the origin" +
            " story of former Special Forces operative turned mercenary Wade Wilson, who after being subjected to a " +
            "rogue experiment that leaves him with accelerated healing powers, adopts the alter ego Deadpool. Armed " +
            "with his new abilities and a dark, twisted sense of humor, Deadpool hunts down the man who nearly " +
            "destroyed his life.\",\n" +
            "      \"release_date\": \"2016-02-09\",\n" +
            "      \"genre_ids\": [\n" +
            "        28,\n" +
            "        12,\n" +
            "        35,\n" +
            "        10749\n" +
            "      ],\n" +
            "      \"id\": 293660,\n" +
            "      \"original_title\": \"Deadpool\",\n" +
            "      \"original_language\": \"en\",\n" +
            "      \"title\": \"Deadpool\",\n" +
            "      \"backdrop_path\": \"/nbIrDhOtUpdD9HKDBRy02a8VhpV.jpg\",\n" +
            "      \"popularity\": 11.592311,\n" +
            "      \"vote_count\": 5129,\n" +
            "      \"video\": false,\n" +
            "      \"vote_average\": 7.17\n" +
            "    },\n" +
            "    {\n" +
            "      \"poster_path\": \"/n8WNMt7stqHUZEE7bEtzK4FwrWe.jpg\",\n" +
            "      \"adult\": false,\n" +
            "      \"overview\": \"Rachel Watson, an alcoholic who divorced her husband Tom after she caught him " +
            "cheating on her, takes the train to work daily. She fantasizes about the relationship of her neighbours," +
            " Scott and Megan Hipwell, during her commute. That all changes when she witnesses something from the " +
            "train window and Megan is missing, presumed dead.\",\n" +
            "      \"release_date\": \"2016-10-06\",\n" +
            "      \"genre_ids\": [\n" +
            "        53\n" +
            "      ],\n" +
            "      \"id\": 346685,\n" +
            "      \"original_title\": \"The Girl on the Train\",\n" +
            "      \"original_language\": \"en\",\n" +
            "      \"title\": \"The Girl on the Train\",\n" +
            "      \"backdrop_path\": \"/fpq86AP0YBYUwNgDvUj5kxwycxH.jpg\",\n" +
            "      \"popularity\": 10.36296,\n" +
            "      \"vote_count\": 69,\n" +
            "      \"video\": false,\n" +
            "      \"vote_average\": 4.87\n" +
            "    },\n" +
            "    {\n" +
            "      \"poster_path\": \"/weUSwMdQIa3NaXVzwUoIIcAi85d.jpg\",\n" +
            "      \"adult\": false,\n" +
            "      \"overview\": \"Thirty years after defeating the Galactic Empire, Han Solo and his allies face a " +
            "new threat from the evil Kylo Ren and his army of Stormtroopers.\",\n" +
            "      \"release_date\": \"2015-12-15\",\n" +
            "      \"genre_ids\": [\n" +
            "        28,\n" +
            "        12,\n" +
            "        878,\n" +
            "        14\n" +
            "      ],\n" +
            "      \"id\": 140607,\n" +
            "      \"original_title\": \"Star Wars: The Force Awakens\",\n" +
            "      \"original_language\": \"en\",\n" +
            "      \"title\": \"Star Wars: The Force Awakens\",\n" +
            "      \"backdrop_path\": \"/c2Ax8Rox5g6CneChwy1gmu4UbSb.jpg\",\n" +
            "      \"popularity\": 10.196726,\n" +
            "      \"vote_count\": 4833,\n" +
            "      \"video\": false,\n" +
            "      \"vote_average\": 7.56\n" +
            "    },\n" +
            "    {\n" +
            "      \"poster_path\": \"/gj282Pniaa78ZJfbaixyLXnXEDI.jpg\",\n" +
            "      \"adult\": false,\n" +
            "      \"overview\": \"Katniss Everdeen reluctantly becomes the symbol of a mass rebellion against the " +
            "autocratic Capitol.\",\n" +
            "      \"release_date\": \"2014-11-18\",\n" +
            "      \"genre_ids\": [\n" +
            "        878,\n" +
            "        12,\n" +
            "        53\n" +
            "      ],\n" +
            "      \"id\": 131631,\n" +
            "      \"original_title\": \"The Hunger Games: Mockingjay - Part 1\",\n" +
            "      \"original_language\": \"en\",\n" +
            "      \"title\": \"The Hunger Games: Mockingjay - Part 1\",\n" +
            "      \"backdrop_path\": \"/83nHcz2KcnEpPXY50Ky2VldewJJ.jpg\",\n" +
            "      \"popularity\": 10.146584,\n" +
            "      \"vote_count\": 3295,\n" +
            "      \"video\": false,\n" +
            "      \"vote_average\": 6.68\n" +
            "    }\n" +
            "  ],\n" +
            "  \"total_results\": 19620,\n" +
            "  \"total_pages\": 981\n" +
            "}");
    }

    /**
     * Parse a String well JSON-formed to a JSON object.
     *
     * @param output
     *            Service Data result
     */
    private void parseOutput(String output) {

        try {
            data = new JSONObject(output);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
