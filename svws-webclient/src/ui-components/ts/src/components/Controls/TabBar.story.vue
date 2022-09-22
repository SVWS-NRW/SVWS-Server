<script lang="ts" setup>
import { ref } from "vue";
import TabBar from './TabBar.vue';
import TabButton from './TabButton.vue';
import TabPanel from './TabPanel.vue';

const additionalTab = ref(false);
const selectedTab = ref(1);
</script>

<template>
  <Story title="SVWS UI/Controls/Tab Bar">
    <Button @click="additionalTab = !additionalTab">Hide/Show additional Tab</Button>
    <TabBar v-model="selectedTab">
      <template #tabs>
        <TabButton>Tab A</TabButton>
        <TabButton>Tab B</TabButton>
        <TabButton disabled>Tab C</TabButton>
        <TabButton :hidden="additionalTab">Tab D</TabButton>
        <TabButton>Tab E</TabButton>
        <TabButton>Tab F</TabButton>
      </template>
      <template #panels>
        <TabPanel>Content für A</TabPanel>
        <TabPanel>Content für B</TabPanel>
        <TabPanel>Content für C</TabPanel>
        <TabPanel :hidden="additionalTab">Content für D</TabPanel>
        <TabPanel>Content für E</TabPanel>
        <TabPanel>Content für F</TabPanel>
      </template>
    </TabBar>
  </Story>
</template>