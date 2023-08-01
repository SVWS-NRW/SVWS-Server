<script setup lang="ts">

	import { ref } from 'vue';
	const value = ref(true);
	const value2 = ref(false);
	const value3 = ref(false);
</script>

<template>
	<Story title="Toggle" id="svws-ui-toggle" icon="ri:toggle-line" :layout="{type: 'grid', width: '45%'}">
		<Variant title="Checked" id="Checked">
			<div class="p-2">
				<svws-ui-toggle v-model="value">Toggle</svws-ui-toggle>
			</div>
		</Variant>

		<Variant title="Unchecked" id="Unchecked">
			<div class="p-2">
				<svws-ui-toggle v-model="value2">Toggle</svws-ui-toggle>
			</div>
		</Variant>

		<Variant title="Statistics" id="Statistics">
			<div class="p-2">
				<svws-ui-toggle v-model="value3" statistics>Toggle</svws-ui-toggle>
			</div>
		</Variant>
	</Story>
</template>
