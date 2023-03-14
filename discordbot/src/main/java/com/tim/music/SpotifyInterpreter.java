package com.tim.music;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.hc.core5.http.ParseException;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.model_objects.specification.ArtistSimplified;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.Playlist;
import se.michaelthelin.spotify.model_objects.specification.PlaylistTrack;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import se.michaelthelin.spotify.requests.data.playlists.GetPlaylistRequest;
import se.michaelthelin.spotify.requests.data.tracks.GetTrackRequest;

public class SpotifyInterpreter {
    
    private SpotifyApi spotifyApi;
    private static SpotifyInterpreter instance;
    private String id;
    private String type;
    public final Dotenv config;

    public SpotifyInterpreter(){
        config = Dotenv.configure().load();
        try{
            initiate();
        }catch(Exception e){
            e.printStackTrace();
        }
        instance = this;
    }

    private void initiate() throws SpotifyWebApiException, IOException, ParseException{
        spotifyApi = new SpotifyApi.Builder()
            .setClientId(config.get("SPOTIFYID"))
            .setClientSecret(config.get("SPOTIFYSECRET"))
            .build();

        ClientCredentialsRequest.Builder request = new ClientCredentialsRequest.Builder(spotifyApi.getClientId(), spotifyApi.getClientSecret());
        ClientCredentials creds = request.grant_type("client_credentials").build().execute();
        spotifyApi.setAccessToken(creds.getAccessToken());
    }

    public ArrayList<String> convert(String link, TextChannel channel) throws ParseException, SpotifyWebApiException, IOException{

        String[] firstSplit = link.split("/");
        String[] secondSplit;

        if(firstSplit.length > 5){
            secondSplit = firstSplit[6].split("\\?");
            type = firstSplit[5];
        }else{
            secondSplit = firstSplit[4].split("\\?");
            type = firstSplit[3];
        }

        id = secondSplit[0];
		ArrayList<String> listOfTracks = new ArrayList<>();
		
		if(type.contentEquals("track")) {

			listOfTracks.add(getArtistAndName(id));
            channel.sendMessage(listOfTracks.get(0)).queue();
			return listOfTracks;
		}
		
		if(type.contentEquals("playlist")) {
			GetPlaylistRequest playlistRequest = spotifyApi.getPlaylist(id).build();
			Playlist playlist = playlistRequest.execute();
			Paging<PlaylistTrack> playlistPaging = playlist.getTracks();
			PlaylistTrack[] playlistTracks = playlistPaging.getItems();
			
			for (PlaylistTrack i : playlistTracks) {
				Track track = (Track) i.getTrack();
				String trackID = track.getId();
				listOfTracks.add(getArtistAndName(trackID));
			}
			
			return listOfTracks;
		}
		
		return null;

    }
    
    private String getArtistAndName(String trackID) throws ParseException, SpotifyWebApiException, IOException {
		String artistNameAndTrackName = "";
		GetTrackRequest trackRequest = spotifyApi.getTrack(trackID).build();
		
		Track track = trackRequest.execute();
		artistNameAndTrackName = track.getName() + " - ";
		
		ArtistSimplified[] artists = track.getArtists();
		for(ArtistSimplified i : artists) {
			artistNameAndTrackName += i.getName() + " ";
		}
	
		return artistNameAndTrackName;
	}

}
