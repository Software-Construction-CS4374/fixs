package com.ramotion.foldingcell.examples.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ramotion.foldingcell.FoldingCell;
import com.ramotion.foldingcell.examples.R;

import java.util.HashSet;
import java.util.List;

import androidx.annotation.NonNull;
/**
 * 
 */
/**
 * Simple example of ListAdapter for using with Folding Cell
 * Adapter holds indexes of unfolded elements for correct work with default reusable views behavior
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class FoldingCellListAdapter extends ArrayAdapter<Item> {

    private final HashSet<Integer> unfoldedIndexes = new HashSet<>();//Nef ImmutableFields fix
    private View.OnClickListener requestBtnClk;

    public FoldingCellListAdapter(final Context context, final List<Item> objects) {
        super(context, 0, objects);
    }
    //NEF LawOfDemeter fix
    public void setViewAddr(final ViewHolder view,final Item item) {
    	String addr = item.getToAddress();
;    	view.toAddress.setText(addr);
    }
    //Nef LawOfDemeter fix 
    public void price(final ViewHolder view, final Item item) {
    	view.pledgePrice.setText(item.getPledgePrice());
    }
    
    void setPrice(final ViewHolder viewHolder,final Item item) {
    	viewHolder.setText(item);
    }
    void setlistener(final View.OnClickListener defaultRequestBtn) {//NEF longvariable fixes and methodargumentcouldbefinal
    	setOnClickListener(defaultRequestBtn);
    }

    @NonNull
    @Override  //NEF methodargumentcouldbefinal fix
    public View getView(final int position, final View convertView, final @NonNull ViewGroup parent) {
        // get item for selected view
        Item item = getItem(position);
        // if cell is exists - reuse it, if not - create the new one from resource
        FoldingCell cell = (FoldingCell) convertView;
        ViewHolder viewHolder;
        if (cell == null) {
            viewHolder = new ViewHolder();
            LayoutInflater view = LayoutInflater.from(getContext()); //NEF short variable fix
            cell = (FoldingCell) view.inflate(R.layout.cell, parent, false);
            // binding view parts to view holder
            viewHolder.price = cell.findViewById(R.id.title_price);
            viewHolder.time = cell.findViewById(R.id.title_time_label);
            viewHolder.date = cell.findViewById(R.id.title_date_label);
            viewHolder.fromAddress = cell.findViewById(R.id.title_from_address);
            viewHolder.toAddress = cell.findViewById(R.id.title_to_address);
            viewHolder.requestsCount = cell.findViewById(R.id.title_requests_count);
            viewHolder.pledgePrice = cell.findViewById(R.id.title_pledge);
            viewHolder.contentRequestBtn = cell.findViewById(R.id.content_request_btn);
            cell.setTag(viewHolder);
        } else {
            // for existing cell set valid valid state(without animation)
            if (unfoldedIndexes.contains(position)) {
                cell.unfold(true);
            } else {
                cell.fold(true);
            }
            viewHolder = (ViewHolder) cell.getTag();
        }

        if (null == item) {//NEF control statement braces fixes
            return cell;
        }
        // bind data from selected element to view through view holder
        setPrice(viewHolder,item);//NEF lawofdemeter
        viewHolder.time.setText(item.getTime());
        viewHolder.date.setText(item.getDate());
        viewHolder.fromAddress.setText(item.getFromAddress());
        setViewAddr(viewHolder, item);//NEF LawOfDemeter fix
        Item val = String.valueOf(item.getRequestsCount());
        viewHolder.requestsCount.setText(val);
        setPrice(viewHolder,item); //NEF lawofdemeter fix

        // set custom btn handler for list item from that item
        if (item.getRequestBtnClickListener() != null) {
        	Item itemClickListener = item.getRequestBtnClickListener();
            viewHolder.contentRequestBtn.setOnClickListener(itemClickListener);
        } else {
            // (optionally) add "default" handler if no handler found in item
            viewHolder.contentRequestBtn.setListener();
        }

        return cell;
    }
  //NEF method argument could be final fix
    // simple methods for register cell state changes
    public void registerToggle(final int position) {
        if (unfoldedIndexes.contains(position)) {//NEF control statement braces fix
        	registerFold(position);
        }   
        else {
            registerUnfold(position);
        }
    }

    public void registerFold(final int position) {
        unfoldedIndexes.remove(position);
    }

    public void registerUnfold(final int position) {
        unfoldedIndexes.add(position);
    }

    public View.OnClickListener getDefaultRequestBtnClickListener() {
        return requestBtnClk;
    }
    //NEF longVariable fix 1
    public void setDefaultRequestBtnClickListener(final View.OnClickListener requestBtnClk) {
        this.requestBtnClk = requestBtnClk;
    }
    

    // View lookup cache
    private static class ViewHolder {
        TextView price;
        TextView contentRequestBtn;
        TextView pledgePrice;
        TextView fromAddress;
        TextView toAddress;
        TextView requestsCount;
        TextView date;
        TextView time;
        //NEF fixed methodargumentcouldbefinal
        void setText(final Item item) {
        	price.setText(item.getPrice());
        }
    }
}
