<script setup lang="ts">
	import SvwsUiToggle from "./Toggle.vue";

	const value = ref(true);
	const value2 = ref(false);
</script>

<template>
	<Story title="SVWS UI/Controls/Toggle">
		<Variant title="Checked">
			<svws-ui-toggle v-model="value">Toggle</svws-ui-toggle>
		</Variant>

		<Variant title="Unchecked">
			<svws-ui-toggle v-model="value2">Toggle</svws-ui-toggle>
		</Variant>

		<Variant title="Statistics">
			<svws-ui-toggle v-model="value2" statistics>Toggle</svws-ui-toggle>
		</Variant>
	</Story>
</template>