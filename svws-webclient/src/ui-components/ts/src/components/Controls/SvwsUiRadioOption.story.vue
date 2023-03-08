<script setup lang="ts">

	const checked = ref('value');
</script>

<template>
	<Story title="SVWS UI/Controls/Radio Option">
		<Variant title="Default">
			<svws-ui-radio-option name="name" value="value" label="Option" />
		</Variant>

		<Variant title="Checked">
			<svws-ui-radio-option v-model="checked" name="name" value="value" label="Option" />
		</Variant>

		<Variant title="Disabled">
			<svws-ui-radio-option name="name" value="value" label="Option" disabled />
		</Variant>

		<Variant title="Statistics">
			<svws-ui-radio-option name="name" value="value" label="Option" statistics />
		</Variant>
	</Story>
</template>