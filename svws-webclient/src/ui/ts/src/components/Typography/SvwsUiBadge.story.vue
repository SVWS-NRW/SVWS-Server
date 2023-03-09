<script setup lang="ts">
</script>

<template>
	<Story title="SVWS UI/Typography/Badge" :layout="{type: 'grid', width: '40%'}">
		<Variant title="Primary">
			<svws-ui-badge type="primary" size="normal">Badge</svws-ui-badge>
		</Variant>

		<Variant title="Success">
			<svws-ui-badge type="success" size="normal">Badge</svws-ui-badge>
		</Variant>

		<Variant title="Error">
			<svws-ui-badge type="error" size="normal">Badge</svws-ui-badge>
		</Variant>

		<Variant title="Highlight">
			<svws-ui-badge type="highlight" size="normal">Badge</svws-ui-badge>
		</Variant>

		<Variant title="Light">
			<svws-ui-badge type="light" size="normal">Badge</svws-ui-badge>
		</Variant>

		<Variant title="Small">
			<svws-ui-badge type="primary" size="small">Badge</svws-ui-badge>
		</Variant>

		<Variant title="Normal">
			<svws-ui-badge type="primary" size="normal">Badge</svws-ui-badge>
		</Variant>

		<Variant title="Medium">
			<svws-ui-badge type="primary" size="medium">Badge</svws-ui-badge>
		</Variant>

		<Variant title="Big">
			<svws-ui-badge type="primary" size="big">Badge</svws-ui-badge>
		</Variant>
	</Story>
</template>