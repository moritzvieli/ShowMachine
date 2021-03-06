package com.ascargon.rocketshow.lighting.designer;

// contains a fixture channela and its fine index, if available
public class FixtureChannelFineIndex {

    // the corresponding channel
    private FixtureChannel fixtureChannel;

    // the corresponding template
    private FixtureProfile fixtureProfile;

    // the name of the channel
    private String channelName;

    // the total fine value count
    private int fineValueCount = 0;

    // the fine value of the current channel
    private int fineIndex = -1;

    public FixtureChannelFineIndex(FixtureChannel fixtureChannel, FixtureProfile fixtureProfile, String channelName, int fineValueCount, int fineIndex) {
        this.fixtureChannel = fixtureChannel;
        this.fixtureProfile = fixtureProfile;
        this.channelName = channelName;
        this.fineValueCount = fineValueCount;
        this.fineIndex = fineIndex;
    }

    public FixtureChannelFineIndex(FixtureProfile fixtureProfile) {
        this.fixtureProfile = fixtureProfile;
    }

    public FixtureChannelFineIndex() {
    }

    public FixtureChannel getFixtureChannel() {
        return fixtureChannel;
    }

    public void setFixtureChannel(FixtureChannel fixtureChannel) {
        this.fixtureChannel = fixtureChannel;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public FixtureProfile getFixtureProfile() {
        return fixtureProfile;
    }

    public void setFixtureProfile(FixtureProfile fixtureProfile) {
        this.fixtureProfile = fixtureProfile;
    }

    public int getFineValueCount() {
        return fineValueCount;
    }

    public void setFineValueCount(int fineValueCount) {
        this.fineValueCount = fineValueCount;
    }

    public int getFineIndex() {
        return fineIndex;
    }

    public void setFineIndex(int fineIndex) {
        this.fineIndex = fineIndex;
    }
}
