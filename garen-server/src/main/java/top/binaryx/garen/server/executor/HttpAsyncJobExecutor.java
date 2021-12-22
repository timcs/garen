//
//
//package top.binaryx.garen.server.executor;
//
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import top.binaryx.garen.common.annotation.ExecutorType;
//import top.binaryx.garen.common.enums.ExecuteStatusEnum;
//import top.binaryx.garen.common.enums.ProtocolTypeEnum;
//import top.binaryx.garen.common.util.HttpClientUtil;
//import top.binaryx.garen.server.common.JobExecuteContext;
//import top.binaryx.garen.server.pojo.dto.HttpJobRequest;
//import top.binaryx.garen.server.pojo.dto.JobConfigDTO;
//import top.binaryx.garen.server.pojo.dto.JobExecLog;
//import top.binaryx.garen.server.service.LogService;
//
//import java.util.Objects;
//
//
//@Slf4j
//@ExecutorType(protocolType = ProtocolTypeEnum.HTTP_ASYNC)
//@Component
//public class HttpAsyncJobExecutor extends AbstractJobExecutor implements JobExecutor {
//
//    @Autowired
//    LogService logService;
//
//    @Value("${garen.server.callbackurl}")
//    String callBackUrl;
//
//    @Override
//    public void execute(JobConfigDTO jobConfigDTO) {
//        log.info("[HttpAsyncJobExecutor.execute] jobConfigDTO:{}", jobConfigDTO);
//        JobExecuteContext context = new JobExecuteContext();
//
//        try {
//            //1.保存数据
//            JobExecLog jobExecLog = buildLog(jobConfigDTO);
//            logService.save(jobExecLog);
//
//            //设置上下文
//            context.setExecuteLog(jobExecLog);
//            context.setJobConfig(jobConfigDTO);
//
//            execute(context);
//        } catch (Exception e) {
//            JobExecLog log = context.getExecuteLog();
//            if (!Objects.isNull(log)) {
//                log.setMemo(StringUtils.abbreviate(e.getMessage(), 512));
//                log.setStatus(ExecuteStatusEnum.FAILED.getValue());
//            } else {
//                return;
//            }
//        }
//    }
//
//    private void execute(JobExecuteContext context) {
//        HttpJobRequest request = new HttpJobRequest();
//        request.setExecuteId(context.getExecuteLog().getId());
//        request.setJobId(context.getJobConfig().getId());
//        request.setJobParam(context.getJobConfig().getJobParam());
//        request.setCallBackUrl(callBackUrl);
//
//        //请求服务
//        HttpClientUtil.asyncPostJson(context.getJobConfig().getUrl(), request);
//    }
//}
