import aggregated.publish.PublishingComponents

plugins {
    id("maven-publish")
}

extensions.create<PublishingComponents>("publishingComponents", project)
