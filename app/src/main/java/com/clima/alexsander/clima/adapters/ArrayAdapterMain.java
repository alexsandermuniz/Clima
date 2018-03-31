package com.clima.alexsander.clima.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.clima.alexsander.clima.R;
import com.clima.alexsander.clima.sqlite.models.City;

public class ArrayAdapterMain extends ArrayAdapter<City> {
	
	private Activity context;
	private List<City> cities;

	static class ViewHolder{
		public TextView name;
		public TextView weather;
		public TextView temp;
	}

	public ArrayAdapterMain(Activity context, List<City> cities)
	{
		super(context, R.layout.item_city_weather,cities);
		this.context=context;
		this.cities = cities;
	}
	@Override
	public View getView(int position,View convertView, ViewGroup parent)
	{
		View rowView = convertView;
		if(rowView ==null)
		{
			LayoutInflater inflater = (LayoutInflater) context.getSystemService
					(Context.LAYOUT_INFLATER_SERVICE) ;			
			rowView=inflater.inflate(R.layout.item_city_weather, parent, false);
			//Create and set the viewHolder components
            ViewHolder viewHolder = new ViewHolder();
			viewHolder.name =  rowView.findViewById(R.id.txtCity);
			viewHolder.weather =  rowView.findViewById(R.id.txtWeather);
			viewHolder.temp =  rowView.findViewById(R.id.txtTemp);
			rowView.setTag(viewHolder);
		
		}
		//Fill the viewHolder infos
		ViewHolder holder = (ViewHolder) rowView.getTag();
		String s = cities.get(position).getName();
		holder.name.setText(s);
		s=cities.get(position).getWeather().getStatusTranslated();
		holder.weather.setText(s);
		s= cities.get(position).getWeather().getTemp()+" °C";
		holder.temp.setText(s);
		
		
		return rowView;
	}
}
