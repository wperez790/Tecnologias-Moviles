package org.moviles.persistance;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import org.moviles.model.Clima;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ClimaRepository {

    private ClimaDAO climaDAO;
    private List<Clima> climas;

    public ClimaRepository(Application application) {
        ClimaRoomDatabase climaRoomDatabase = ClimaRoomDatabase.getDatabase(application);
        climaDAO = climaRoomDatabase.climaDAO();
    }

    public List<Clima> getAllClimas() {
        try {
            climas = new getClimasAsyncTask(climaDAO).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return climas;
    }

    public void insert (Clima clima){
        try{
        new insertClimasAsyncTask(climaDAO).execute(clima);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void insertAllClimas (List<Clima> climas){
        try{
            for (int i = 0; i<climas.size(); i++){
                new insertClimasAsyncTask(climaDAO).execute(climas.get(i));
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private static class getClimasAsyncTask extends AsyncTask<Void, Void, List<Clima>> {

        private ClimaDAO asyncTaskClimaDao;

        getClimasAsyncTask(ClimaDAO climaDAO) {

            asyncTaskClimaDao = climaDAO;

        }


        @Override
        protected List<Clima> doInBackground(Void... voids) {
            return asyncTaskClimaDao.getClimas();
        }
    }


    private static class insertClimasAsyncTask extends AsyncTask<Clima, Void, Void> {

        private ClimaDAO asyncTaskClimaDao;

        insertClimasAsyncTask(ClimaDAO climaDAO){
            asyncTaskClimaDao = climaDAO;
        }


        @Override
        protected Void doInBackground(Clima... climas) {
            asyncTaskClimaDao.insert(climas[0]);
            return null;
        }
    }



    }