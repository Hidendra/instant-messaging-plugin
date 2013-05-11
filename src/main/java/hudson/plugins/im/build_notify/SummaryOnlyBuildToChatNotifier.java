package hudson.plugins.im.build_notify;

import hudson.Extension;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.ResultTrend;
import hudson.plugins.im.IMPublisher;
import hudson.plugins.im.tools.BuildHelper;
import hudson.plugins.im.tools.MessageHelper;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.IOException;

import static hudson.plugins.im.tools.BuildHelper.*;

/**
 * {@link BuildToChatNotifier} that sends out a brief one line summary.
 *
 * @author Kohsuke Kawaguchi
 */
public class SummaryOnlyBuildToChatNotifier extends BuildToChatNotifier {
    @DataBoundConstructor
    public SummaryOnlyBuildToChatNotifier() {
    }

    @Override
    public String buildStartMessage(IMPublisher publisher, AbstractBuild<?, ?> build, BuildListener listener) throws IOException, InterruptedException {
        return Messages.SummaryOnlyBuildToChatNotifier_StartMessage(build.getDisplayName(),getProjectName(build));
    }

    @Override
    public String buildCompletionMessage(IMPublisher publisher, AbstractBuild<?, ?> build, BuildListener listener) throws IOException, InterruptedException {
        final StringBuilder sb;
        if (BuildHelper.isFix(build)) {
            sb = new StringBuilder(Messages.SummaryOnlyBuildToChatNotifier_BuildIsFixed());
        } else {
            sb = new StringBuilder();
        }
        
        ResultTrend trend = ResultTrend.getResultTrend(build);
        String status;
        
        switch (trend) {
            case SUCCESS:
            case FIXED:
                status = "\u0002\u000303BUILD SUCCESS!\u000f";
                status = "\u0002\u000303BUILD SUCCESS!\u000f";
                break;
            case FAILURE:
                status = "\u0002\u000304FAILED\u000f";
                break;
        
            default:
                status = "\u0002\u000308" + trend.getID() + "\u000f";
        }
        
        sb.append(Messages.SummaryOnlyBuildToChatNotifier_Summary(
                getProjectName(build), build.getDisplayName(),
                status,
                build.getTimestampString(),
                MessageHelper.getBuildURL(build)));

        return sb.toString();
    }

    @Extension
    public static class DescriptorImpl extends BuildToChatNotifierDescriptor {
        public String getDisplayName() {
            return "Just summary";
        }
    }
}
