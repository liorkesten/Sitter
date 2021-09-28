package service.sitter.recommendations;

import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import service.sitter.db.DataBase;
import service.sitter.db.IDataBase;
import service.sitter.models.Connection;
import service.sitter.models.Parent;
import service.sitter.models.Recommendation;
import service.sitter.utils.calls.Call;
import service.sitter.utils.calls.CallType;
import service.sitter.utils.calls.CallsUtils;

public class CallLogsRecommendationProvider implements IRecommendationProvider {
    private static final String TAG = CallLogsRecommendationProvider.class.getSimpleName();


    @Override
    public void getRecommendations(Context context, String userID) {
        Log.d(TAG, "called to CallLogsRecommendationProvider with userId:" + userID);
        IDataBase db = DataBase.getInstance();
        List<Call> calls = getCallDetails(context);
        Log.d(TAG, "calls" + calls);
        List<Call> sortedUniqueCalls = CallsUtils.sortUniqueCallsByTheirFreq(calls);

        List<String> phoneNumbers = sortedUniqueCalls.stream().map(Call::getPhNumber).collect(Collectors.toList());

        db.getParentsByPhoneNumbers(phoneNumbers, new IGetParents() {

            @Override
            public void apply(List<Parent> parents) {
                db.getConnectionsOfParents(parents, new IGetConnections() {

                    @Override
                    public void apply(List<Connection> connectionsOfParents) {
                        db.getConnectionsOfParent(userID, new IGetConnections() {

                            @Override
                            public void apply(List<Connection> userConnections) {

                                Set<String> userConnectionsIDs = userConnections.stream().map(Connection::getSideBUId).collect(Collectors.toSet());
                                List<Recommendation> recommendations = new ArrayList<>();
                                Set<String> seenBanysitters = new HashSet<>();
                                for (Connection connection : connectionsOfParents) {
                                    String candidateBabysitterID = connection.getSideBUId();
                                    if (userConnectionsIDs.contains(candidateBabysitterID) ||
                                            seenBanysitters.contains(candidateBabysitterID)) {
                                        continue;
                                    }

                                    seenBanysitters.add(candidateBabysitterID);
                                    Connection connectionForRec = new Connection(userID, candidateBabysitterID);
                                    Recommendation newRecommendation = new Recommendation(connectionForRec, TAG, 10 /*TODO score by the order*/, "");
                                    recommendations.add(newRecommendation);
                                }
                                // Add all recommendations.
                                db.addRecommendations(recommendations);
                            }
                        });
                    }
                });
            }
        });
    }


    private static List<Call> getCallDetails(Context context) {
        Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI,
                null, null, null, CallLog.Calls.DATE + " DESC");
        int number = cursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = cursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = cursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = cursor.getColumnIndex(CallLog.Calls.DURATION);
        List<Call> calls = new ArrayList<>();
        while (cursor.moveToNext()) {
            String phNumber = cursor.getString(number);
            String callDate = cursor.getString(date);
            Date callDayTime = new Date(Long.parseLong(callDate));
            String callDuration = cursor.getString(duration);

            String callTypeStr = cursor.getString(type);
            CallType callType = null;
            int dircode = Integer.parseInt(callTypeStr);

            calls.add(new Call(phNumber, callDate, callDayTime, callDuration, CallType.values()[dircode]));
        }
        cursor.close();

        return calls;
    }

}

