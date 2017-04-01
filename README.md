# VK SmartFeed
###### ITMO, Computer Science. Second year winter practice. Styagov A., P3210.
The main idea of this project is to create an Android app for viewing [VK feed](https://vk.com/feed) with some extra features unavailable by default [VK API](https://vk.com/dev/).
## Key features:
- [ ] **Individual feed list** (*temp name*)
> You can form them not only from certain sources (communities and users), but more specific due to filters, e.g. show/hide posts containing specified hashtags (either single: `#example`, or ones relative to communities: `#for@example`), only posts with likes more than N, etc.
- [ ] Notifications
> Sending notifications to users about compiled list or hottest posts from their **individual feed list(-s)** with the option of selecting notification parameters:
>  - when to send notifications (day, time, periodicity)
>  - where to send notifications (e-mail, vk private message by bot, android push notification  (either re-direct to e-mail, or open application), in-app information screen when next time app will be opened after notification sended; or several of these ways!)
>  - what should be in the message (select one or many of his embedded VK feed lists and/or **individual feed lists** to track).
- [ ] Feed lists switching
> You can switch between many of feed lists. Besides all the lists user already have he can create as many **individual feed lists** as he wants (*hope it'll be so.*).
- [ ] Improved Black List
> New Black List will give you possibilities to track specified hashtags, posts authors, or even concrete words/phrases/emojis in posts from your feed list to automatically hide them from it).
## Other features:
- [x] Fully functional feed view (showing all post information including implementation of "Like", "Comment" and "Share" buttons, post options button, support of all VK attachment types)
- [x] Parse for 5 types of spans in post/comment message: 
  ```
  [id123|Wiki-link]
  #single_hashtag
  #community@inner_hashtag
  just_an@email.com
  http://all-types.of/links
  ```
- [ ] Handle actions on these spans.
- [ ] Note view with comments where you can leave your own.
- [ ] Community/User view.
