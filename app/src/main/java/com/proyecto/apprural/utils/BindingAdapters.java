package com.proyecto.apprural.utils;

import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.proyecto.apprural.model.beans.Bed;
import com.proyecto.apprural.model.beans.FullAccommodationOffer;
import com.proyecto.apprural.model.beans.Prohibition;
import com.proyecto.apprural.model.beans.Property;
import com.proyecto.apprural.model.beans.Room;
import com.proyecto.apprural.model.beans.Service;

import java.util.List;

public class BindingAdapters {
    @BindingAdapter("priceWithCurrency")
    public static void setPriceWithCurrency(TextView textView, double price) {
        textView.setText(String.format("%.2f €", price));
    }

    @BindingAdapter("quantityBeds")
    public static void setQuantityBeds(TextView textView, int quantity) {
        if(quantity > 1) {
            textView.setText(String.valueOf(quantity)+ " unidades");
        }else {
            textView.setText(String.valueOf(quantity)+ " unidad");
        }
    }

    @BindingAdapter("location")
    public static void setLocation(TextView textView, Property property) {
        textView.setText(property.getAddress() +", "+property.getTown() + ", "+property.getCountry());
    }

    @BindingAdapter("capacity")
    public static void setCapacity(TextView textView, int capacity) {
        if(capacity > 1) {
            textView.setText(String.valueOf(capacity)+ " personas");
        }else {
            textView.setText(String.valueOf(capacity)+ " persona");
        }
    }

    @BindingAdapter("includedServices")
    public static void setIncludedServices(TextView textView, List<Service> serviceList) {
        textView.setText(generateServiceIncluded(serviceList));
    }

    @BindingAdapter("extraServices")
    public static void setExtraServices(TextView textView, List<Service> serviceList) {
        textView.setText(generateServiceExtra(serviceList));
    }

    @BindingAdapter("prohibitionsText")
    public static void setProhibitionsText(TextView textView, List<Prohibition> prohibitions) {
        textView.setText(generateProhibitions(prohibitions));
    }

    @BindingAdapter("accommodationLocation")
    public static void setAccommodationLocation(TextView textView, FullAccommodationOffer offer) {
        StringBuilder textBuilder = new StringBuilder("Localización: ");
        boolean hasLocation = false;

        if (!offer.getAddress().isEmpty()) {
            textBuilder.append(offer.getAddress()).append(", ");
            hasLocation = true;
        }
        if (!offer.getTown().isEmpty()) {
            textBuilder.append(offer.getTown()).append(", ");
            hasLocation = true;
        }
        if (!offer.getCountry().isEmpty()) {
            textBuilder.append(offer.getCountry());
            hasLocation = true;
        }

        if (!hasLocation) {
            textBuilder.append("No tiene");
        } else {
            // Eliminar la última coma y espacio si hay algún texto
            int length = textBuilder.length();
            if (textBuilder.charAt(length - 2) == ',') {
                textBuilder.delete(length - 2, length);
            }
        }

        textView.setText(textBuilder.toString());

    }

    @BindingAdapter("bedsText")
    public static void setBeds(TextView textView, List<Bed> beds) {
        String text = generateBeds(beds);
        textView.setText(text);
    }

    @BindingAdapter("roomsText")
    public static void setRoomsText(TextView textView, List<Room> roomList) {
        StringBuilder sb = new StringBuilder();
        if(roomList != null) {
            for(Room room : roomList) {
                sb.append("Nombre: ").append(room.getName()).append("\n");
                sb.append("Categoría: ").append(room.getCategory()).append("\n");
                sb.append(generateServiceIncluded(room.getServices())).append("\n");
                sb.append(generateServiceExtra(room.getServices())).append("\n");
                sb.append("Camas: ").append(generateBeds(room.getBeds())).append("\n\n");
            }
            textView.setText(sb.toString());
        }else {
            textView.setText("No hay habitaciones");
        }
    }

    public static String generateServiceIncluded(List<Service> serviceList) {
        String text = "";
        int index = 0;
        if(serviceList != null) {
            for(Service service : serviceList) {
                ++index;
                if(service.isIncluded()) {
                    if(index==serviceList.size()) {
                        text += service.getName();
                    }else {
                        text += service.getName() + ", ";
                    }
                }
            }
            if(!text.isEmpty()) {
                return "Servicios incluidos: "+text;
            }else {
                return "Sin servicios incluidos.";
            }
        }
        else {
            return "Sin servicios incluidos.";
        }
    }

    public static String generateServiceExtra(List<Service> serviceList) {
        String text = "";
        int index = 0;
        if(serviceList != null) {
            for(Service service : serviceList) {
                ++index;
                if(!service.isIncluded()) {
                    if(index==serviceList.size()) {
                        text += service.getName() + "("+ service.getPrice()+" €)";
                    }else {
                        text += service.getName()+ "("+ service.getPrice()+" €)" + ", ";
                    }
                }
            }
            if(!text.isEmpty()) {
                return "Servicios extra: "+text;
            }else {
                return "Sin servicios extra.";
            }
        }
        else {
            return "Sin servicios extra.";
        }
    }

    public static String generateProhibitions(List<Prohibition> prohibitions) {
        String text = "Prohibiciones: ";
        int index = 0;
        if (prohibitions != null) {
            for (Prohibition prohibition : prohibitions) {
                ++index;
                if(index==prohibitions.size()) {
                    text += prohibition.getName();
                }else {
                    text += prohibition.getName()+ ", ";
                }
            }
            return text;
        } else {
            return "Sin prohibiciones.";
        }
    }

    public static String generateBeds(List<Bed> beds) {
        StringBuilder sb = new StringBuilder();
        if(beds != null) {
            for (Bed bed : beds) {
                sb.append(bed.getType()).append("(").append(bed.getQuantity()).append(")").append(", ");
            }
            if (sb.length() > 0) {
                sb.setLength(sb.length() - 2); // Elimina la última coma y espacio
            }
            return sb.toString();
        }else {
            return "No hay camas.";
        }
    }
}
