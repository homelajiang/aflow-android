package com.anglll.aflow.data.model;

import java.util.List;

public class Feed {

    /**
     * id : 5acdec4a035f6dac8ca997f3
     * type : {"id":1,"value":"videos","name":"视频"}
     * channel : 0
     * contentId : 4305762
     * title : 史上最大烤串儿@野食小哥
     * description : 史上最大烤串儿，你要不要来一串试试？<br/>欢迎关注微信微博@野食小哥
     * cover : http://imgs.aixifan.com/content/2018_04_11/1523435476.jpg
     * releaseDate : 1523440942000
     * visit : {"views":6771,"comments":66,"score":624,"danmakuSize":0}
     * owner : {"id":4551417,"name":"野食小哥","avatar":"http://cdn.aixifan.com/dotnet/artemis/u/cms/www/201609/09150117c4em3oen.jpg"}
     * attachment : [{"source":"5950408","description":"Part1","danmakuId":"5950408"}]
     * source : {"name":"AcFun","url":"http://www.acfun.cn/"}
     */

    private String id;
    private TypeBean type;
    private int channel;
    private String contentId;
    private String title;
    private String description;
    private String cover;
    private long releaseDate;
    private VisitBean visit;
    private OwnerBean owner;
    private SourceBean source;
    private List<AttachmentBean> attachment;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TypeBean getType() {
        return type;
    }

    public void setType(TypeBean type) {
        this.type = type;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public long getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(long releaseDate) {
        this.releaseDate = releaseDate;
    }

    public VisitBean getVisit() {
        return visit;
    }

    public void setVisit(VisitBean visit) {
        this.visit = visit;
    }

    public OwnerBean getOwner() {
        return owner;
    }

    public void setOwner(OwnerBean owner) {
        this.owner = owner;
    }

    public SourceBean getSource() {
        return source;
    }

    public void setSource(SourceBean source) {
        this.source = source;
    }

    public List<AttachmentBean> getAttachment() {
        return attachment;
    }

    public void setAttachment(List<AttachmentBean> attachment) {
        this.attachment = attachment;
    }

    public static class TypeBean {
        /**
         * id : 1
         * value : videos
         * name : 视频
         */

        private int id;
        private String value;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class VisitBean {
        /**
         * views : 6771
         * comments : 66
         * score : 624
         * danmakuSize : 0
         */

        private int views;
        private int comments;
        private int score;
        private int danmakuSize;

        public int getViews() {
            return views;
        }

        public void setViews(int views) {
            this.views = views;
        }

        public int getComments() {
            return comments;
        }

        public void setComments(int comments) {
            this.comments = comments;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public int getDanmakuSize() {
            return danmakuSize;
        }

        public void setDanmakuSize(int danmakuSize) {
            this.danmakuSize = danmakuSize;
        }
    }

    public static class OwnerBean {
        /**
         * id : 4551417
         * name : 野食小哥
         * avatar : http://cdn.aixifan.com/dotnet/artemis/u/cms/www/201609/09150117c4em3oen.jpg
         */

        private int id;
        private String name;
        private String avatar;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    }

    public static class SourceBean {
        /**
         * name : AcFun
         * url : http://www.acfun.cn/
         */

        private String name;
        private String url;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class AttachmentBean {
        /**
         * source : 5950408
         * description : Part1
         * danmakuId : 5950408
         */

        private String source;
        private String description;
        private String danmakuId;

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDanmakuId() {
            return danmakuId;
        }

        public void setDanmakuId(String danmakuId) {
            this.danmakuId = danmakuId;
        }
    }
}
