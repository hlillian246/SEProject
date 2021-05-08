package edu.nyu.se2440.loadmovies.utils;

import edu.nyu.se2440.loadmovies.models.MovieModel;
import edu.nyu.se2440.loadmovies.models.TMMovieModel;
import edu.nyu.se2440.loadmovies.models.TmdbCrewModel;

public class Utils {

    public static MovieModel mapToMovieModel(TMMovieModel tmMoviemodel)
    {
        MovieModel result = new MovieModel();

        result.setMovieId(tmMoviemodel.getImdb_id());
        result.setTitle(tmMoviemodel.getTitle());

        boolean flag = false;
        for (TmdbCrewModel crew : tmMoviemodel.getCredits().getCrew())
        {
            if ("Director".equals(crew.getJob())) {
                result.setDirector(crew.getName());
                flag = true;
                break;
            }
        }

        if (!flag) result.setDirector("");

        if (tmMoviemodel.getRelease_date() != null && tmMoviemodel.getRelease_date().length() > 4) {
            result.setYear(Integer.parseInt(tmMoviemodel.getRelease_date().substring(0, 4)));
        }
        else
        {
            result.setYear(0);
        }

        result.setOverview(tmMoviemodel.getOverview());
        result.setRevenue(tmMoviemodel.getRevenue());
        result.setRating(tmMoviemodel.getVote_average());
        result.setNumVotes(tmMoviemodel.getVote_count());

        return result;
    }



}
