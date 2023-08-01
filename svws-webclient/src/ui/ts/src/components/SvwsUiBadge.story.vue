<script setup lang="ts">
</script>

<template>
	<Story title="Badge" id="svws-ui-badge" :layout="{type: 'grid', width: '45%'}" icon="ri:shield-line">
		<Variant title="Primary" id="Primary">
			<svws-ui-badge type="primary" size="normal">Badge</svws-ui-badge>
		</Variant>

		<Variant title="Success" id="Success">
			<svws-ui-badge type="success" size="normal">Badge</svws-ui-badge>
		</Variant>

		<Variant title="Error" id="Error">
			<svws-ui-badge type="error" size="normal">Badge</svws-ui-badge>
		</Variant>

		<Variant title="Highlight" id="Highlight">
			<svws-ui-badge type="highlight" size="normal">Badge</svws-ui-badge>
		</Variant>

		<Variant title="Light" id="Light">
			<svws-ui-badge type="light" size="normal">Badge</svws-ui-badge>
		</Variant>

		<Variant title="Small" id="Small">
			<svws-ui-badge type="primary" size="small">Badge</svws-ui-badge>
		</Variant>

		<Variant title="Normal" id="Normal">
			<svws-ui-badge type="primary" size="normal">Badge</svws-ui-badge>
		</Variant>

		<Variant title="Medium" id="Medium">
			<svws-ui-badge type="primary" size="medium">Badge</svws-ui-badge>
		</Variant>

		<Variant title="Big" id="Big">
			<svws-ui-badge type="primary" size="big">Badge</svws-ui-badge>
		</Variant>
	</Story>
</template>
