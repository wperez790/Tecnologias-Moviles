package org.moviles.persistance;

import android.app.Application;
import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;

import org.moviles.model.Clima;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ClimaRepository {

    private ClimaDAO climaDAO;
    private List<Clima> climas;
    private Clima clima;

    public ClimaRepository(Application application) {
        ClimaRoomDatabase climaRoomDatabase = ClimaRoomDatabase.getDatabase(application);
        climaDAO = climaRoomDatabase.climaDAO();
        clima = new Clima();
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

    public Clima getClimaByCity(String city) {
        try {
            clima = new getClimaAsyncTask(climaDAO).execute(city).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return clima;
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

    public Clima getClimaByLocation(Location location) {
        try {
            clima = new getClimaByLocationAsyncTask(climaDAO).execute(location).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return clima;
    }

    public List<Clima> getLastClimas(Integer cantidad){
        List<Clima> climaList= new ArrayList<>();
        try{
                climaList = new getLastClimasAsyncTask(climaDAO).execute(cantidad).get();


        }
        catch (Exception e){
            e.printStackTrace();
        }

        return climaList;
    }



    ////////////////////////////////////////////////////////////////////////////////////////////
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

    private static class getClimaAsyncTask extends AsyncTask<String, Void, Clima> {

        private ClimaDAO asyncTaskClimaDao;

        getClimaAsyncTask(ClimaDAO climaDAO) {

            asyncTaskClimaDao = climaDAO;

        }


        @Override
        protected Clima doInBackground(String... ciudades) {
            return asyncTaskClimaDao.getClimaByCity(ciudades[0]);
        }
    }


    private class getClimaByLocationAsyncTask extends AsyncTask<Location, Void, Clima>{
        private ClimaDAO asyncTaskClimaDao;
        public getClimaByLocationAsyncTask(ClimaDAO climaDAO) {
            asyncTaskClimaDao = climaDAO;
        }

        @Override
        protected Clima doInBackground(Location... locations) {
            Double lat = locations[0].getLatitude();
            Double lon = locations[0].getLongitude();
            return asyncTaskClimaDao.getClimaByLocation(lat, lon);
        }
    }


    private static class getLastClimasAsyncTask extends AsyncTask <Integer, Void, List<Clima> >{
        private ClimaDAO asyncTaskClimaDao;
        public getLastClimasAsyncTask(ClimaDAO climaDAO){asyncTaskClimaDao = climaDAO;}

        @Override
        protected List<Clima> doInBackground(Integer... integers) {
            return asyncTaskClimaDao.getLastClimas(integers[0]);
        }
    }
}