# AntiGenerationLag

This plugin has been built to avoid lag when exploring unloaded chunks especially for servers that allows hacked clients.

By default, the config should not punish vanilla players.

## How it works

Every time a chunk loads, this plugin will find which player loaded it, and makes an average of how many chunks have been loaded by that player in the last few seconds.

If that number is above a threshold, the player will be rubber banded (teleported a few blocks back).

## How to configure

The best way to configure this plugin is to enable `debug` in the config and give yourself the permissions `antigenerationlag.debug` and `antigenerationlag.norubberband` (being OP should work as well).

With that permission, you won't get any rubber banding, and you'll get your current chunks per second load count.

![Debug message](https://user-images.githubusercontent.com/2182934/90948423-b4116200-e40c-11ea-8f0e-600890acf87e.png)

The color of the message is important here:

- If the text is green, it means you're not near the limit of getting rubber banded
- If it turns orange, it means you're above 80%, that's how it should be when traveling as desired full speed
- If it is red, it means you would have been rubber banded if you didn't have the permission.

Knowing your current speed while loading terrain and generating terrain should allow you to tweak the rubber band threshold as needed.
Keep in mind that by default, a newly generated chunk counts as 8 chunks, that's why that number may look big.

## Permissions

- `antigenerationlag.debug`: displays the action bar for debugging purposes
- `antigenerationlag.norubberband`: disables rubber banding

## Contributing

You are free to suggest changes by opening an issue ticket.

You can also open PRs, remember to bump the version in `pom.xml` and `plugin.yml` before opening a pull request.
