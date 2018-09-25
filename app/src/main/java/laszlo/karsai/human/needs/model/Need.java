package laszlo.karsai.human.needs.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Need implements Parcelable{

    private String name;
    private int index;
    private Double score;
    private Integer absolutelyAgreeNum;
    private Integer agreeNum;
    private Integer partlyAgreeNum;
    private Integer disagreeNum;
    private Integer absolutelyDisagreeNum;

    public Need(
            String name,
            int index,
            double score,
            int absolutelyAgreeNum,
            int agreeNum,
            int partlyAgreeNum,
            int disagreeNum,
            int absolutelyDisagreeNum) {
        this.name = name;
        this.index = index;
        this.score = score;
        this.absolutelyAgreeNum = absolutelyAgreeNum;
        this.agreeNum = agreeNum;
        this.partlyAgreeNum = partlyAgreeNum;
        this.disagreeNum = disagreeNum;
        this.absolutelyDisagreeNum = absolutelyDisagreeNum;
    }

    private Need(Parcel in) {
        name = in.readString();
        index = in.readInt();
        score = in.readDouble();
        absolutelyAgreeNum = in.readInt();
        agreeNum = in.readInt();
        partlyAgreeNum = in.readInt();
        disagreeNum = in.readInt();
        absolutelyDisagreeNum = in.readInt();
    }

    public static final Creator<Need> CREATOR = new Creator<Need>() {
        @Override
        public Need createFromParcel(Parcel in) {
            return new Need(in);
        }

        @Override
        public Need[] newArray(int size) {
            return new Need[size];
        }
    };

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }

    public Double getScore() {
        return score;
    }

    public Integer getAbsolutelyAgreeNum() {
        return absolutelyAgreeNum;
    }

    public Integer getAgreeNum() {
        return agreeNum;
    }

    public Integer getPartlyAgreeNum() {
        return partlyAgreeNum;
    }

    public Integer getDisagreeNum() {
        return disagreeNum;
    }

    public Integer getAbsolutelyDisagreeNum() {
        return absolutelyDisagreeNum;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeInt(index);
        parcel.writeDouble(score);
        parcel.writeInt(absolutelyAgreeNum);
        parcel.writeInt(agreeNum);
        parcel.writeInt(partlyAgreeNum);
        parcel.writeInt(disagreeNum);
        parcel.writeInt(absolutelyDisagreeNum);
    }
}
