package in.mitrevels.mitrevels.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Vibrator;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import in.mitrevels.mitrevels.R;
import in.mitrevels.mitrevels.activities.CategoryActivity;
import in.mitrevels.mitrevels.activities.EasterEggActivity;
import in.mitrevels.mitrevels.models.categories.CategoryModel;
import in.mitrevels.mitrevels.utilities.HandyMan;

/**
 * Created by anurag on 3/2/17.
 */
public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder> {

    private List<CategoryModel> categoriesList;
    private Activity activity;
    private String mText = "";
    private static final String CHEAT_CODE = "fannymagnet";

    public CategoriesAdapter(List<CategoryModel> categoriesList, Activity activity) {
        this.categoriesList = categoriesList;
        this.activity = activity;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CategoryViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_category, parent, false));
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        CategoryModel category = categoriesList.get(position);
        holder.catName.setText(category.getCategoryName());
        holder.catLogo.setImageResource(HandyMan.help().getCategoryLogo(category.getCategoryID()));
    }

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        ImageView catLogo;
        TextView catName;

        public CategoryViewHolder(View itemView) {
            super(itemView);

            catLogo = (ImageView)itemView.findViewById(R.id.cat_logo_image_view);
            catName = (TextView)itemView.findViewById(R.id.cat_name_text_view);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(activity, CategoryActivity.class);
            intent.putExtra("catName", categoriesList.get(getAdapterPosition()).getCategoryName());
            intent.putExtra("catID", categoriesList.get(getAdapterPosition()).getCategoryID());
            intent.putExtra("catDesc", categoriesList.get(getAdapterPosition()).getCategoryDescription());
            activity.startActivity(intent);
            activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_diagonal);
        }

        @Override
        public boolean onLongClick(final View view) {
            if (view.getId() == itemView.getId()){
                if (categoriesList.get(getAdapterPosition()).getCategoryName().toLowerCase().equals("gaming")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    final View ccView = View.inflate(activity, R.layout.dialog_enter_cheat_code, null);
                    builder.setView(ccView);

                    // Set up the buttons
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            EditText input = (EditText)ccView.findViewById(R.id.cheat_code_edit_text);
                            mText = input.getText().toString();

                            CoordinatorLayout rootLayout = (CoordinatorLayout)activity.findViewById(R.id.main_activity_coordinator_layout);

                            if (mText.toLowerCase().equals(CHEAT_CODE)){
                                Snackbar.make(rootLayout, "Cheat activated!", Snackbar.LENGTH_SHORT).show();
                                Vibrator v = (Vibrator)activity.getSystemService(Context.VIBRATOR_SERVICE);
                                v.vibrate(1000);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(activity, EasterEggActivity.class);
                                        activity.startActivity(intent);
                                    }
                                }, 1000);
                            }
                            else{
                                Snackbar.make(rootLayout, "Cheat rejected!", Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();
                }
                else{
                    onClick(view);
                }
            }

            return true;
        }
    }
}
