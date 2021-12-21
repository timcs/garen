package top.binaryx.garen.common;


public interface Constant {

    String SLANT = "/";

    String JOB_ID = "jobId";

    String SERVER_URL = "http://%s:%d/server";

    String MIGRATE_URL = SERVER_URL + "/admin/migrate";

    String OPTION_URL = SERVER_URL + "/admin/job/ops";

    String LOAD_IP_URL = SERVER_URL + "/admin/load/ip";

    String PROPERTY = "${%s}";

    String SYSTEM = "system";

    String ZK_NAMESPACE = "garen";

}
