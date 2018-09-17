package site.skylake.project_cs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Adaperdetailrev extends RecyclerView.Adapter<Adaperdetailrev.AdaperdetailrevViewHolder> {

    private Context mcontext;
    private ArrayList<Itemdetailrev> mlistdetailrev;

    public Adaperdetailrev(Context context, ArrayList<Itemdetailrev> listdetailrev){
        mcontext = context;
        mlistdetailrev = listdetailrev;
    }

    @NonNull
    @Override
    public AdaperdetailrevViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mcontext).inflate(R.layout.itemrevdetail, parent, false);
        return new AdaperdetailrevViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaperdetailrevViewHolder holder, int position) {
        Itemdetailrev current = mlistdetailrev.get(position);

        String img = current.getMimg();
        String namebook = current.getMnamebook();

        holder.textView.setText(namebook);
        Picasso.get().load(img).fit().centerInside().into(holder.imageView);

    }

    @Override
    public int getItemCount() {
//        return 0;
        return mlistdetailrev.size();
    }


    public class AdaperdetailrevViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;
        public TextView textView;

        public AdaperdetailrevViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.img_rev_deteail);
            textView = itemView.findViewById(R.id.text_rev_deteail);
        }
    }

}
