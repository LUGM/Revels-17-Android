package in.mitrevels.mitrevels.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import in.mitrevels.mitrevels.R;
import in.mitrevels.mitrevels.models.sports.SportsModel;

/**
 * Created by anurag on 21/2/17.
 */

public class SportsQualifiedTeamsAdapter extends RecyclerView.Adapter<SportsQualifiedTeamsAdapter.QualifiedTeamViewHolder> {

    private List<SportsModel> resultsList;
    private Context context;

    public SportsQualifiedTeamsAdapter(List<SportsModel> resultsList, Context context) {
        this.resultsList = resultsList;
        this.context = context;
    }

    @Override
    public QualifiedTeamViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new QualifiedTeamViewHolder(LayoutInflater.from(context).inflate(R.layout.item_qualified_teams, parent, false));
    }

    @Override
    public void onBindViewHolder(QualifiedTeamViewHolder holder, int position) {
        SportsModel result = resultsList.get(position);
        holder.teamID.setText(result.getTeamID());
    }

    @Override
    public int getItemCount() {
        return resultsList.size();
    }

    public class QualifiedTeamViewHolder extends RecyclerView.ViewHolder {
        TextView teamID;

        public QualifiedTeamViewHolder(View itemView) {
            super(itemView);

            teamID = (TextView)itemView.findViewById(R.id.qualified_teams_id_text_view);
        }
    }
}
