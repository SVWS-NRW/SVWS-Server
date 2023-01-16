<script setup lang="ts">
	import SvwsUiIcon from "./Icon.vue";
</script>

<template>
	<Story title="SVWS UI/Layout/Icon" :layout="{ type: 'grid', width: '40%' }">
		<Variant title="Line">
			<svws-ui-icon icon="home" variant="line" />
		</Variant>

		<Variant title="Fill">
			<svws-ui-icon icon="home" variant="fill" />
		</Variant>
	</Story>
</template>
