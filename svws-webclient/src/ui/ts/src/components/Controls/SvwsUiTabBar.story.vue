<script lang="ts" setup>
	import { ref } from "vue";

	const additionalTab = ref(false);
	const selectedTab = ref(1);
</script>

<template>
	<Story title="SVWS UI/Controls/Tab Bar">
		<svws-ui-button @click="additionalTab = !additionalTab">Hide/Show additional Tab</svws-ui-button>
		<svws-ui-tab-bar v-model="selectedTab">
			<template #tabs>
				<svws-ui-tab-button>Tab A</svws-ui-tab-button>
				<svws-ui-tab-button>Tab B</svws-ui-tab-button>
				<svws-ui-tab-button disabled>Tab C</svws-ui-tab-button>
				<svws-ui-tab-button :hidden="additionalTab">Tab D</svws-ui-tab-button>
				<svws-ui-tab-button>Tab E</svws-ui-tab-button>
				<svws-ui-tab-button>Tab F</svws-ui-tab-button>
			</template>
			<template #panels>
				<svws-ui-tab-panel>Content für A</svws-ui-tab-panel>
				<svws-ui-tab-panel>Content für B</svws-ui-tab-panel>
				<svws-ui-tab-panel>Content für C</svws-ui-tab-panel>
				<svws-ui-tab-panel :hidden="additionalTab">Content für D</svws-ui-tab-panel>
				<svws-ui-tab-panel>Content für E</svws-ui-tab-panel>
				<svws-ui-tab-panel>Content für F</svws-ui-tab-panel>
			</template>
		</svws-ui-tab-bar>
	</Story>
</template>