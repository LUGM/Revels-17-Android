package in.mitrevels.revels.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.mitrevels.revels.R;
import in.mitrevels.revels.fragments.ResultsFragment;
import in.mitrevels.revels.models.results.ResultModel;

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ResultViewHolder> {

    private List<ResultsFragment.EventResultModel> resultsList;
    private Context context;

    public ResultsAdapter(List<ResultsFragment.EventResultModel> resultsList, Context context) {
        this.resultsList = resultsList;
        this.context = context;
    }

    @Override
    public ResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ResultViewHolder(LayoutInflater.from(context).inflate(R.layout.item_result, parent, false));
    }

    @Override
    public void onBindViewHolder(ResultViewHolder holder, int position) {
        ResultsFragment.EventResultModel result = resultsList.get(position);

        holder.eventName.setText(result.eventName);
        holder.eventCategory.setText(result.eventCategory);
    }

    @Override
    public int getItemCount() {
        return resultsList.size();
    }

    class ResultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView eventName;
        ImageView catLogo;
        TextView eventCategory;
        ImageView expandIcon;

        public ResultViewHolder(View itemView) {
            super(itemView);

            eventName = (TextView)itemView.findViewById(R.id.result_event_name_text_view);
            catLogo = (ImageView)itemView.findViewById(R.id.result_cat_logo_image_view);
            eventCategory = (TextView)itemView.findViewById(R.id.result_cat_text_view);
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

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            TextView eventName = (TextView)bottomSheetView.findViewById(R.id.result_dialog_event_name_text_view);
            eventName.setText(resultsList.get(getAdapterPosition()).eventName);

            RecyclerView teamsRecyclerView = (RecyclerView)bottomSheetView.findViewById(R.id.result_dialog_teams_recycler_view);
            teamsRecyclerView.setAdapter(new QualifiedTeamsAdapter(resultsList.get(getAdapterPosition()).eventResultsList, context));
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
