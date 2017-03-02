package in.revels.revels.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import in.revels.revels.R;
import in.revels.revels.fragments.RevelsCupFragment;

public class SportsResultsAdapter extends RecyclerView.Adapter<SportsResultsAdapter.ResultViewHolder> {

    private List<RevelsCupFragment.SportsResultModel> resultsList;
    private Context context;

    public SportsResultsAdapter(List<RevelsCupFragment.SportsResultModel> resultsList, Context context) {
        this.resultsList = resultsList;
        this.context = context;
    }

    @Override
    public ResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ResultViewHolder(LayoutInflater.from(context).inflate(R.layout.item_result, parent, false));
    }

    @Override
    public void onBindViewHolder(ResultViewHolder holder, int position) {
        RevelsCupFragment.SportsResultModel result = resultsList.get(position);

        holder.eventName.setText(result.eventName);
        holder.eventRound.setText(result.eventRound);
        holder.catLogo.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return resultsList.size();
    }

    class ResultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView catLogo;
        TextView eventName;
        TextView eventRound;
        ImageView expandIcon;

        public ResultViewHolder(View itemView) {
            super(itemView);

            catLogo = (ImageView)itemView.findViewById(R.id.result_cat_logo_image_view);
            eventName = (TextView)itemView.findViewById(R.id.result_event_name_text_view);
            eventRound = (TextView)itemView.findViewById(R.id.result_round_text_view);
            expandIcon = (ImageView)itemView.findViewById(R.id.result_expand_image_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            expandIcon.setImageResource(R.drawable.ic_minus);
            View bottomSheetView = View.inflate(context, R.layout.dialog_results, null);
            final BottomSheetDialog dialog = new BottomSheetDialog(context);

            dialog.setContentView(bottomSheetView);

            BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) bottomSheetView.getParent());
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

            TextView eventName = (TextView)bottomSheetView.findViewById(R.id.result_dialog_event_name_text_view);
            eventName.setText(resultsList.get(getAdapterPosition()).eventName);

            TextView eventRound = (TextView)bottomSheetView.findViewById(R.id.result_dialog_round_text_view);
            eventRound.setText(resultsList.get(getAdapterPosition()).eventRound);

            RecyclerView teamsRecyclerView = (RecyclerView)bottomSheetView.findViewById(R.id.result_dialog_teams_recycler_view);
            teamsRecyclerView.setAdapter(new SportsQualifiedTeamsAdapter(resultsList.get(getAdapterPosition()).eventResultsList, context));
            teamsRecyclerView.setLayoutManager(new GridLayoutManager(context, 2));

            dialog.show();

            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    expandIcon.setImageResource(R.drawable.ic_plus);
                }
            });
        }
    }

}
