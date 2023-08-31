<script setup lang="ts">

	import { ref } from 'vue';
	const value = ref(true);
	const value2 = ref(false);
	const value3 = ref(false);
</script>

<template>
	<Story title="Toggle (Deprecated)" id="svws-ui-toggle" icon="ri:alert-line" :layout="{type: 'grid', width: '45%'}" icon-color="#f00">
		<Variant title="Checked" id="Checked">
			Deprecated: Wird von <strong><code>{{ `<svws-ui-checkbox type="toggle" ... />` }}</code></strong> abgelöst.
		</Variant>

		<!--<Variant title="Unchecked" id="Unchecked">
			<div class="p-2">
				<svws-ui-toggle v-model="value2">Toggle</svws-ui-toggle>
			</div>
		</Variant>

		<Variant title="Statistics" id="Statistics">
			<div class="p-2">
				<svws-ui-toggle v-model="value3" statistics>Toggle</svws-ui-toggle>
			</div>
		</Variant>-->
	</Story>
</template>
