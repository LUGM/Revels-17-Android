package in.mitrevels.mitrevels.adapters;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import in.mitrevels.mitrevels.R;
import in.mitrevels.mitrevels.activities.EventActivity;
import in.mitrevels.mitrevels.fragments.FavouritesFragment;
import in.mitrevels.mitrevels.models.FavouritesModel;
import in.mitrevels.mitrevels.receivers.NotificationReceiver;
import in.mitrevels.mitrevels.utilities.HandyMan;
import io.realm.Realm;

/**
 * Created by anurag on 5/1/17.
 */
public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.FavouriteViewHolder>{

    private Activity activity;
    private List<FavouritesModel> favouritesList;
    boolean flag = false;
    private FavouritesFragment parentFragment;
    private Realm mRealm;

    public FavouritesAdapter(Activity activity, List<FavouritesModel> favouritesList, FavouritesFragment parentFragment, Realm mRealm) {
        this.activity = activity;
        this.favouritesList = favouritesList;
        this.parentFragment = parentFragment;
        this.mRealm = mRealm;
    }

    @Override
    public FavouriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FavouriteViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_favourite, parent, false));
    }

    @Override
    public void onBindViewHolder(final FavouriteViewHolder holder, int position) {
        FavouritesModel favourite = favouritesList.get(position);

        holder.favouriteName.setText(favourite.getEventName());
        holder.favouriteTime.setText(favourite.getStartTime());

        int logoID = HandyMan.help().getCategoryLogo(favourite.getCatID());
        holder.favouriteLogo.setImageResource(logoID);

        if (!favourite.getRound().equals("-")){
            if (favourite.getRound().toLowerCase().equals("finals") || favourite.getRound().toLowerCase().equals("f") || favourite.getRound().toLowerCase().equals("final")){
                holder.eventRound.setText("RF");
            }
            else if(favourite.getRound().toLowerCase().equals("prelims")) {
                holder.eventRound.setText("RP");
            }
            else{
                holder.eventRound.setText("R"+favourite.getRound());
            }
        }
        else{
            holder.eventRound.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return favouritesList.size();
    }

    private void removeFavourite(FavouritesModel favourite){
        mRealm.beginTransaction();
        mRealm.where(FavouritesModel.class).equalTo("id", favourite.getId()).findAll().deleteAllFromRealm();
        mRealm.commitTransaction();
    }

    public void removeNotification(FavouritesModel favourite){
        Intent intent = new Intent(activity, NotificationReceiver.class);
        intent.putExtra("eventName", favourite.getEventName());
        intent.putExtra("startTime", favourite.getStartTime());
        intent.putExtra("eventVenue", favourite.getVenue());
        intent.putExtra("eventID", favourite.getId());

        AlarmManager alarmManager = (AlarmManager)activity.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(activity, Integer.parseInt(favourite.getCatID()+favourite.getId()+"0"), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(activity, Integer.parseInt(favourite.getCatID()+favourite.getId()+"1"), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pendingIntent1);
        alarmManager.cancel(pendingIntent2);
    }

    class FavouriteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView favouriteLogo;
        TextView favouriteName;
        TextView favouriteTime;
        ImageView deleteFavourite;
        FrameLayout logoFrame;
        TextView eventRound;

        public FavouriteViewHolder(View itemView) {
            super(itemView);

            favouriteLogo = (ImageView)itemView.findViewById(R.id.fav_logo_image_view);
            favouriteName = (TextView)itemView.findViewById(R.id.fav_name_text_view);
            favouriteTime = (TextView)itemView.findViewById(R.id.fav_time_text_view);
            deleteFavourite = (ImageView)itemView.findViewById(R.id.favourite_item_delete);
            logoFrame = (FrameLayout)itemView.findViewById(R.id.fav_event_logo_frame);
            eventRound = (TextView)itemView.findViewById(R.id.fav_event_round_text_view);

            itemView.setOnClickListener(this);
            deleteFavourite.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            if (view.getId() == deleteFavourite.getId()){
                if (favouritesList.size() == 1){
                    if (parentFragment != null) parentFragment.removeAllFavourites(favouritesList.get(getAdapterPosition()).getDay());
                }
                else{
                    View alertView = View.inflate(activity, R.layout.dialog_alert, null);
                    final BottomSheetDialog dialog = new BottomSheetDialog(activity);

                    dialog.setContentView(alertView);

                    BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) alertView.getParent());
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                    TextView alertText = (TextView)alertView.findViewById(R.id.alert_text_view);
                    TextView cancel = (TextView)alertView.findViewById(R.id.alert_cancel_text_view);
                    TextView confirm = (TextView)alertView.findViewById(R.id.alert_ok_button);

                    alertText.setText(R.string.favourite_alert_message);
                    dialog.show();

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.hide();
                        }
                    });

                    confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.hide();
                            removeNotification(favouritesList.get(getAdapterPosition()));
                            removeFavourite(favouritesList.get(getAdapterPosition()));
                            Snackbar.make(activity.findViewById(R.id.main_activity_coordinator_layout), favouritesList.get(getAdapterPosition()).getEventName()+" removed from favourites!", Snackbar.LENGTH_SHORT).show();
                            favouritesList.remove(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition());
                        }
                    });
                }
            }
            else{
                Intent intent = new Intent(activity, EventActivity.class);
                FavouritesModel favourite = favouritesList.get(getAdapterPosition());
                intent.putExtra("Event ID", favourite.getId());
                intent.putExtra("Event Name", favourite.getEventName());
                intent.putExtra("Event Round", favourite.getRound());
                intent.putExtra("Event Date", favourite.getDate());
                intent.putExtra("Event Start Time", favourite.getStartTime());
                intent.putExtra("Event End Time", favourite.getEndTime());
                intent.putExtra("Event Venue", favourite.getVenue());
                intent.putExtra("Team Of", favourite.getParticipants());
                intent.putExtra("Event Category", favourite.getCatName());
                intent.putExtra("Category ID", favourite.getCatID());
                intent.putExtra("Event Day", favourite.getDay());
                intent.putExtra("Contact Number", favourite.getContactNumber());
                intent.putExtra("Contact Name", "("+favourite.getContactName()+")");
                intent.putExtra("Event Description", favourite.getDescription());
                intent.putExtra("Favourite", true);
                intent.putExtra("Category Logo", getAdapterPosition()%2);
                intent.putExtra("enableFavourite", false);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation(activity, logoFrame, activity.getString(R.string.cat_logo_transition));
                    activity.startActivity(intent, options.toBundle());
                }
                else {
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_diagonal);
                }

            }
        }
    }
}
