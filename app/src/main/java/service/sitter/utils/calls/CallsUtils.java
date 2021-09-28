package service.sitter.utils.calls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class CallsUtils {
    public static List<Call> sortUniqueCallsByTheirFreq(List<Call> calls) {
        Map<String, Integer> callsFreq = new HashMap<>();
        calls.forEach(call -> callsFreq.put(call.getPhNumber(),
                callsFreq.getOrDefault(call.getPhNumber(), 0) + 1)
        );
        // Remove duplicates.
        List<Call> uniqueCalls = new ArrayList<Call>(new HashSet<>(calls));
        uniqueCalls.sort((c1, c2) -> {
            Integer c1Freq = callsFreq.get(c1.getPhNumber());
            Integer c2Freq = callsFreq.get(c2.getPhNumber());
            return Integer.compare(c1Freq, c2Freq);
        });
        return uniqueCalls;
    }
}


