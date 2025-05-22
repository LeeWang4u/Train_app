package com.example.train_app.adapters.recyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.train_app.R;
import com.example.train_app.dto.response.RouteBasedTicketCountResponse;

import java.util.List;

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.RouteViewHolder> {
    private List<RouteBasedTicketCountResponse> routeList;

    public RouteAdapter(List<RouteBasedTicketCountResponse> routes) {
        this.routeList = routes;
    }

    @NonNull
    @Override
    public RouteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_route, parent, false);
        return new RouteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RouteViewHolder holder, int position) {
        RouteBasedTicketCountResponse route = routeList.get(position);
        holder.title.setText(route.getFrom() + " -> " + route.getTo());
        holder.rank.setText("Top " + (position + 1));
    }

    @Override
    public int getItemCount() {
        return routeList.size();
    }

    public static class RouteViewHolder extends RecyclerView.ViewHolder {
        TextView title, rank;

        public RouteViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.routeText);
            rank = itemView.findViewById(R.id.rankText);
        }
    }
}
