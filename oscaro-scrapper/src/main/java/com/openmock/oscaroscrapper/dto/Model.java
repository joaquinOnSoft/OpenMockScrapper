package com.openmock.oscaroscrapper.dto;

import com.openmock.util.DateUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.text.ParseException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Setter
@Getter
public class Model extends AbstractVehicle {
    @Setter(AccessLevel.NONE)
    private List<Type> types;
    private Date manufacturedFrom;
    private Date manufacturedTo;

    public Model(String id, String name) {
        super(id, name);
        types = new LinkedList<>();
        setDateRange();
    }

    public void addTypes(List<Type> pType) {
        if (pType != null) {
            types.addAll(pType);
        }
    }

    public void addType(Type type) {
        if (type != null) {
            types.add(type);
        }
    }

    public Type getType(int position) {
        Type type = null;
        if (position >= 0 && position < types.size()) {
            type = types.get(position);
        }
        return type;
    }

    private void setDateRange() {
        Pattern pattern = Pattern.compile("(0[1-9]|1[0-2])\\/\\d{4}", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(name);
        boolean first = true;
        Date date;

        while (matcher.find()) {
            try {
                date = DateUtil.strToDate(matcher.group(), "MM/yyyy");

                if (first) {
                    manufacturedFrom = date;
                    first = false;
                } else {
                    manufacturedTo = date;
                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
