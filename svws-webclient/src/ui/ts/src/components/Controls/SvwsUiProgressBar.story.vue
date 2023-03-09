<script setup lang="ts">
	import { logEvent } from 'histoire/client';
</script>

<template>
	<Story title="SVWS UI/Controls/Progress Bar">
		<Variant title="Normal">
			<svws-ui-progress-bar :progress="82" size="normal">Progress Bar</svws-ui-progress-bar>
		</Variant>

		<Variant title="Small">
			<svws-ui-progress-bar :progress="41" size="small">Progress Bar</svws-ui-progress-bar>
		</Variant>
	</Story>
</template>