package car.pubnub.api.endpoints.channel_groups;

import car.pubnub.api.PubNub;
import car.pubnub.api.PubNubException;
import car.pubnub.api.builder.PubNubErrorBuilder;
import car.pubnub.api.endpoints.Endpoint;
import car.pubnub.api.enums.PNOperationType;
import car.pubnub.api.managers.RetrofitManager;
import car.pubnub.api.managers.TelemetryManager;
import car.pubnub.api.models.consumer.channel_group.PNChannelGroupsAllChannelsResult;
import car.pubnub.api.models.server.Envelope;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.experimental.Accessors;
import retrofit2.Call;
import retrofit2.Response;

@Accessors(chain = true, fluent = true)
public class AllChannelsChannelGroup extends Endpoint<Envelope<Object>, PNChannelGroupsAllChannelsResult> {

    private String channelGroup;

    public AllChannelsChannelGroup(PubNub pubnub, TelemetryManager telemetryManager, RetrofitManager retrofit) {
        super(pubnub, telemetryManager, retrofit);
    }

    @Override
    protected List<String> getAffectedChannels() {
        return null;
    }

    @Override
    protected List<String> getAffectedChannelGroups() {
        return null;
    }

    @Override
    protected void validateParams() throws PubNubException {
        if (channelGroup == null || channelGroup.isEmpty()) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_GROUP_MISSING).build();
        }
    }

    @Override
    protected Call<Envelope<Object>> doWork(Map<String, String> params) {
        return this.getRetrofit().getChannelGroupService()
                .allChannelsChannelGroup(this.getPubnub().getConfiguration().getSubscribeKey(), channelGroup, params);
    }

    @Override
    protected PNChannelGroupsAllChannelsResult createResponse(Response<Envelope<Object>> input) throws PubNubException {
        Map<String, Object> stateMappings;

        if (input.body() == null || input.body().getPayload() == null) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_PARSING_ERROR).build();
        }

        stateMappings = (Map<String, Object>) input.body().getPayload();
        List<String> channels = (ArrayList<String>) stateMappings.get("channels");

        return PNChannelGroupsAllChannelsResult.builder()
                .channels(channels)
                .build();
    }

    @Override
    protected PNOperationType getOperationType() {
        return PNOperationType.PNChannelsForGroupOperation;
    }

    @Override
    protected boolean isAuthRequired() {
        return true;
    }

    @java.lang.SuppressWarnings("all")
    @javax.annotation.Generated("lombok")
    public AllChannelsChannelGroup channelGroup(final String channelGroup) {
        this.channelGroup = channelGroup;
        return this;
    }
}
