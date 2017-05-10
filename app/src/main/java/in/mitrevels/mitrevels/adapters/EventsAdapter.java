package in.mitrevels.mitrevels.adapters;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import in.mitrevels.mitrevels.R;
import in.mitrevels.mitrevels.activities.EventActivity;
import in.mitrevels.mitrevels.models.FavouritesModel;
import in.mitrevels.mitrevels.models.events.EventModel;
import in.mitrevels.mitrevels.receivers.NotificationReceiver;
import in.mitrevels.mitrevels.utilities.HandyMan;
import io.realm.Realm;

/**
 * Created by anurag on 6/12/16.
 */
public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventViewHolder> {

    private List<EventModel> eventsList;
    private List<EventModel> allEvents;
    private Activity activity;
    private Realm mDatabase;
    private static final int ADD_FAVOURITE = 0;
    private static final int REMOVE_FAVOURITE = 1;
    private final int CREATE_NOTIFICATION = 0;
    private final int CANCEL_NOTIFICATION = 1;

    public EventsAdapter(Activity activity, List<EventModel> eventsList, List<EventModel> allEvents, Realm mDatabase) {
        this.activity = activity;
        this.eventsList = eventsList;
        this.allEvents = allEvents;
        this.mDatabase = mDatabase;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        EventModel event = eventsList.get(position);
        holder.eventName.setText(event.getEventName());
        holder.eventVenue.setText(event.getVenue());
        holder.eventTime.setText(event.getStartTime()+" - "+event.getEndTime());
        holder.eventLogo.setImageResource(HandyMan.help().getCategoryLogo(event.getCatId()));

        holder.eventRound.setVisibility(View.VISIBLE);

        if (event.getRound() != null && !event.getRound().equals("-") && !event.getRound().equals("")){
            if (event.getRound().toLowerCase().charAt(0) == 'r')
                holder.eventRound.setText(event.getRound().toUpperCase());
            else
                holder.eventRound.setText("R"+event.getRound().toUpperCase().charAt(0));
        }
        else{
            holder.eventRound.setVisibility(View.GONE);
        }

        FavouritesModel favourite = null;

        if (mDatabase != null) favourite = mDatabase.where(FavouritesModel.class).equalTo("eventName", event.getEventName()).equalTo("date", event.getDate()).findFirst();

        if(favourite != null) {
            holder.eventFav.setColorFilter(ContextCompat.getColor(activity, R.color.red));
            holder.eventFav.setTag("Selected");
        }
        else{
            holder.eventFav.setColorFilter(ContextCompat.getColor(activity, R.color.fav_deselect));
            holder.eventFav.setTag("Deselected");
        }

    }

    public void addOrRemoveFavourites(EventModel event, int operation){
        if (mDatabase == null) return;

        if(operation == ADD_FAVOURITE) {
            FavouritesModel favourite = new FavouritesModel();

            favourite.setId(event.getEventId());
            favourite.setCatID(event.getCatId());
            favourite.setEventName(event.getEventName());
            favourite.setRound(event.getRound());
            favourite.setVenue(event.getVenue());
            favourite.setDate(event.getDate());
            favourite.setDay(event.getDay());
            favourite.setStartTime(event.getStartTime());
            favourite.setEndTime(event.getEndTime());
            favourite.setParticipants(event.getEventMaxTeamNumber());
            favourite.setContactName(event.getContactName());
            favourite.setContactNumber(event.getContactNumber());
            favourite.setCatName(event.getCatName());
            favourite.setDescription(event.getDescription());

            mDatabase.beginTransaction();
            mDatabase.copyToRealm(favourite);
            mDatabase.commitTransaction();

            editNotification(event, CREATE_NOTIFICATION);
        }
        else if (operation == REMOVE_FAVOURITE){
            mDatabase.beginTransaction();
            mDatabase.where(FavouritesModel.class).equalTo("eventName", event.getEventName()).equalTo("day", event.getDay()).findAll().deleteAllFromRealm();
            mDatabase.commitTransaction();

            editNotification(event, CANCEL_NOTIFICATION);
        }

    }

    private void editNotification(EventModel event, int operation){
        Intent intent = new Intent(activity, NotificationReceiver.class);
        intent.putExtra("eventName", event.getEventName());
        intent.putExtra("startTime", event.getStartTime());
        intent.putExtra("eventVenue", event.getVenue());
        intent.putExtra("eventID", event.getEventId());

        AlarmManager alarmManager = (AlarmManager)activity.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(activity, Integer.parseInt(event.getCatId()+event.getEventId()+"0"), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(activity, Integer.parseInt(event.getCatId()+event.getEventId()+"1"), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (operation==CREATE_NOTIFICATION){
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa", Locale.US);
            Date d = null;

            try {
                d = sdf.parse(event.getStartTime());
            } catch (ParseException e) {
                e.printStackTrace();
                return;
            }

            int eventDate = 7 + Integer.parseInt(event.getDay());   //event dates start from 8th March

            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(d);

            calendar1.set(Calendar.MONTH, Calendar.MARCH);
            calendar1.set(Calendar.YEAR, 2017);
            calendar1.set(Calendar.DATE, eventDate);
            calendar1.set(Calendar.SECOND, 0);

            long eventTimeInMillis = calendar1.getTimeInMillis();
            calendar1.set(Calendar.HOUR_OF_DAY, calendar1.get(Calendar.HOUR_OF_DAY)-1);

            Calendar calendar2 = Calendar.getInstance();

            Log.d("Calendar 1", calendar1.getTimeInMillis()+"");
            Log.d("Calendar 2", calendar2.getTimeInMillis()+"");

            if(calendar2.getTimeInMillis() <= eventTimeInMillis)
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar1.getTimeInMillis(), pendingIntent1);

            Calendar calendar3 = Calendar.getInstance();
            calendar3.set(Calendar.SECOND, 0);
            calendar3.set(Calendar.MINUTE, 0);
            calendar3.set(Calendar.HOUR, 0);
            calendar3.set(Calendar.AM_PM, Calendar.AM);
            calendar3.set(Calendar.MONTH, Calendar.MARCH);
            calendar3.set(Calendar.YEAR, 2017);
            calendar3.set(Calendar.DATE, eventDate);

            Log.d("Calendar 3", calendar3.getTimeInMillis()+"");

            if (calendar2.getTimeInMillis() < calendar3.getTimeInMillis()){
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar3.getTimeInMillis(), pendingIntent2);
                Log.d("Alarm", "set");
            }

        }
        else if (operation==CANCEL_NOTIFICATION){
            alarmManager.cancel(pendingIntent1);
            alarmManager.cancel(pendingIntent2);
        }

    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }

    public void filterResults(String query){
        eventsList.clear();

        for (EventModel event : allEvents){
            if (event.getEventName().toLowerCase().contains(query.toLowerCase()) || event.getCatName().toLowerCase().equals(query.toLowerCase()) || event.getHashtag().toLowerCase().equals(query.toLowerCase()) || event.getEventType().toLowerCase().equals(query.toLowerCase()))
                eventsList.add(event);
        }

        notifyDataSetChanged();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView eventLogo;
        TextView eventName;
        TextView eventTime;
        TextView eventVenue;
        ImageView eventFav;
        TextView eventRound;

        public EventViewHolder(View itemView) {
            super(itemView);

            eventLogo = (ImageView)itemView.findViewById(R.id.event_logo_image_view);
            eventName = (TextView)itemView.findViewById(R.id.event_name_text_view);
            eventTime = (TextView)itemView.findViewById(R.id.event_time_text_view);
            eventVenue = (TextView)itemView.findViewById(R.id.event_venue_text_view);
            eventFav = (ImageView)itemView.findViewById(R.id.event_fav_image_view);
            eventRound = (TextView)itemView.findViewById(R.id.event_round_text_view);

            eventFav.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            try {
                if (v.getId() == eventFav.getId()){
                    String name = eventsList.get(getAdapterPosition()).getEventName();
                    if (eventFav.getTag().toString().equals("Deselected")){
                        eventFav.setTag("Selected");
                        eventFav.setColorFilter(ContextCompat.getColor(activity, R.color.red));
                        Snackbar.make(activity.findViewById(R.id.main_activity_coordinator_layout), eventName.getText()+" added to favourites!", Snackbar.LENGTH_SHORT).show();

                        addOrRemoveFavourites(eventsList.get(getAdapterPosition()), ADD_FAVOURITE);
                    }
                    else{
                        eventFav.setTag("Deselected");
                        eventFav.setColorFilter(ContextCompat.getColor(activity, R.color.fav_deselect));
                        Snackbar.make(activity.findViewById(R.id.main_activity_coordinator_layout), eventName.getText()+" removed from favourites!", Snackbar.LENGTH_SHORT).show();

                        addOrRemoveFavourites(eventsList.get(getAdapterPosition()), REMOVE_FAVOURITE);
                    }
                }

                if (v.getId() == itemView.getId()){
                    Intent intent = new Intent(activity, EventActivity.class);
                    EventModel event = eventsList.get(getAdapterPosition());
                    intent.putExtra("Event ID", event.getEventId());
                    intent.putExtra("Event Name", event.getEventName());
                    intent.putExtra("Event Round", event.getRound());
                    intent.putExtra("Event Date", event.getDate());
                    intent.putExtra("Event Start Time", event.getStartTime());
                    intent.putExtra("Event End Time", event.getEndTime());
                    intent.putExtra("Event Venue", event.getVenue());
                    intent.putExtra("Team Of", event.getEventMaxTeamNumber());
                    intent.putExtra("Event Category", event.getCatName());
                    intent.putExtra("Category ID", event.getCatId());
                    intent.putExtra("Event Day", event.getDay());
                    intent.putExtra("Contact Number", event.getContactNumber());
                    intent.putExtra("Contact Name", "("+event.getContactName()+")");
                    intent.putExtra("Event Description", event.getDescription());
                    intent.putExtra("enableFavourite", true);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        final View statusBar = activity.findViewById(android.R.id.statusBarBackground);
                        final View navigationBar = activity.findViewById(android.R.id.navigationBarBackground);

                        final List<Pair<View, String>> pairs = new ArrayList<>();

                        if (statusBar != null)
                            pairs.add(Pair.create(statusBar, Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME));

                        if (navigationBar != null)
                            pairs.add(Pair.create(navigationBar, Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME));

                        pairs.add(Pair.create((View)eventLogo, activity.getString(R.string.cat_logo_transition)));

                        if (pairs.size() > 0){
                            ActivityOptionsCompat options = ActivityOptionsCompat.
                                    makeSceneTransitionAnimation(activity, pairs.toArray(new Pair[pairs.size()]));
                            activity.startActivity(intent, options.toBundle());
                        }

                    }
                    else {
                        activity.startActivity(intent);
                        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_diagonal);
                    }
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
    }
}
