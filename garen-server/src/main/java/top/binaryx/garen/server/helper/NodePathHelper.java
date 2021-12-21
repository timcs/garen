package top.binaryx.garen.server.helper;

import org.apache.commons.lang3.StringUtils;


public final class NodePathHelper {
    public final static String SERVER_NODE = "/server";
    public final static String SERVER_IP_NODE = "/server/ip";
    public final static String SERVER_IP_FORMAT = "/server/ip/%s";
    public final static String SERVER_IP_REGEX = "/server/ip/((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}";
    public final static String SERVER_LATCH_NODE = "/server/latch";
    public final static String SERVER_LEADER_NODE = "/server/leader";

    public final static String GROUPS_NODE = "/groups";
    public final static String GROUP_NODE = "/groups/%s";
    public final static String GROUP_NODE_REGEX = "/groups/[0-9]{9}";

    public final static String REMOVE_NODE = "/groups/%s/remove";
    public final static String REMOVE_NODE_REGEX = "/groups/[0-9]{9}/remove";

    public final static String OWNER_NODE = "/groups/%s/owner";
    public final static String OWNER_NODE_REGEX = "/groups/[0-9]{9}/owner";

    public final static String FLOWER_NODE = "/groups/%s/jobs/%s/flower";

    public final static String JOBS_NODE = "/groups/%s/jobs";
    public final static String JOBS_NODE_REGEX = "/groups/[0-9]{9}/jobs";

    public final static String JOB_NODE = "/groups/%s/jobs/%s";
    public final static String JOB_NODE_REGEX = "/groups/[0-9]{9}/jobs/[0-9]{18}";

    public static String getJobsNode(Integer group) {
        return String.format(JOBS_NODE, group);
    }

    public static String getJobNode(Integer groupId, Long jobId) {
        return String.format(JOB_NODE, groupId, jobId);
    }

    public static String getServerIpNode() {
        return SERVER_IP_NODE;
    }

    public static String getServerNode() {
        return SERVER_NODE;
    }

    public static String getGroupsNode() {
        return GROUPS_NODE;
    }

    public static String getGroupNode(Integer group) {
        return String.format(GROUP_NODE, group);
    }

    public static String getOwnerNode(Integer group) {
        return String.format(OWNER_NODE, group);
    }

    public static String getServerIpNode(String ip) {
        return String.format(SERVER_IP_FORMAT, ip);
    }

    public static String getJobIdFromPath(String path) {
        String jobId = StringUtils.substringAfterLast(path, "jobs/");
        return jobId.length() == 18 ? jobId : jobId.substring(0, 18);
    }

    public static String getGroupIdFromPath(String path) {
        String groupId = StringUtils.substringAfterLast(path, "groups/");
        return groupId.length() == 9 ? groupId : groupId.substring(0, 9);
    }

    public static Integer getGroupIdFromJobId(Long jobId) {
        return Integer.parseInt(jobId.toString().substring(0, 9));
    }

    public static boolean isJobNode(String path) {
        return path.matches(JOB_NODE_REGEX);
    }

    public static boolean isGroupsNode(String path) {
        return path.length() == 17 && path.matches(GROUP_NODE_REGEX);
    }

    public static boolean isRemoveNode(String path) {
        return path.matches(REMOVE_NODE_REGEX);
    }

    public static boolean isOwnerNode(String path) {
        return path.matches(OWNER_NODE_REGEX);
    }

    public static boolean isServerIpNode(String path) {
        return path.matches(SERVER_IP_REGEX);
    }

    public static  String  getServerLeaderNode() {
        return SERVER_LEADER_NODE;
    }

}
